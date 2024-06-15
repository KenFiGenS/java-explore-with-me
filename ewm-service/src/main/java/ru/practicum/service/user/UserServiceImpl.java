package ru.practicum.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentDtoCreate;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestDtoAfterChangeStatus;
import ru.practicum.dto.request.RequestDtoChangeStatus;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.*;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLDataException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public EventDtoForResponse createEvent(int userId, EventDtoCreate eventDtoCreate) {
        if (eventDtoCreate.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Дата начала события менее чем через 2 часа");
        }
        Category currentCategory = categoryRepository.getReferenceById(eventDtoCreate.getCategory());
        User initiator = userRepository.getReferenceById(userId);
        Event currentEvent = EventMapper.toEvent(eventDtoCreate, currentCategory, initiator);
        Event currentEventAfterSave = eventRepository.save(currentEvent);
        return EventMapper.toEventDtoForResponse(currentEventAfterSave);
    }

    @Override
    public RequestDto createRequest(int userId, int eventId) {
        Event currentEvent = eventRepository.getReferenceById(eventId);
        List<Request> requestsByEventId = requestRepository.findByEvent(eventId);
        if (userId == currentEvent.getInitiator().getId()) {
            throw new IllegalArgumentException("The user is the initiator of this event");
        }
        if (!currentEvent.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalArgumentException("The event has not been published yet");
        }
        if (!currentEvent.isRequestModeration() && currentEvent.getParticipantLimit() <= requestsByEventId.size()) {
            throw new IllegalArgumentException("The event gathered the maximum number of participants");
        }
        Request newRequest = RequestMapper.toRequest(userId, eventId);
        if (currentEvent.isRequestModeration() && currentEvent.getParticipantLimit() == 0) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
            currentEvent.setConfirmedRequests(currentEvent.getConfirmedRequests() + 1);
        }
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    public EventDtoForResponse updateEvent(int userId, int eventId, EventDtoUserUpdate eventDtoUserUpdate) {
        Event eventForUpdate = eventRepository.getReferenceById(eventId);
        if (eventForUpdate.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalArgumentException("Cannot update the event because it's not in the right state: PUBLISHED");
        }
        if (eventDtoUserUpdate.getAnnotation() != null)
            eventForUpdate.setAnnotation(eventDtoUserUpdate.getAnnotation());
        if (eventDtoUserUpdate.getCategory() != 0) {
            Category categoryForUpdate = categoryRepository.getReferenceById(eventDtoUserUpdate.getCategory());
            eventForUpdate.setCategory(categoryForUpdate);
        }
        if (eventDtoUserUpdate.getDescription() != null)
            eventForUpdate.setDescription(eventDtoUserUpdate.getDescription());
        if (eventDtoUserUpdate.getEventDate() != null) {
            if (eventDtoUserUpdate.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                eventForUpdate.setEventDate(eventDtoUserUpdate.getEventDate());
            } else {
                throw new IllegalArgumentException("Время начала события менее чем через 2 часа");
            }
        }

        if (eventDtoUserUpdate.getLocation() != null) {
            eventForUpdate.setLat(eventDtoUserUpdate.getLocation().getLat());
            eventForUpdate.setLon(eventDtoUserUpdate.getLocation().getLon());
        }
        if (eventDtoUserUpdate.getPaid() != null) eventForUpdate.setPaid(eventDtoUserUpdate.getPaid());
        if (eventDtoUserUpdate.getParticipantLimit() > 0)
            eventForUpdate.setParticipantLimit(eventDtoUserUpdate.getParticipantLimit());
        if (eventDtoUserUpdate.getRequestModeration() != null)
            eventForUpdate.setRequestModeration(eventDtoUserUpdate.getRequestModeration());
        if (eventDtoUserUpdate.getStateAction() != null) {
            if (eventDtoUserUpdate.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                eventForUpdate.setState(EventStatus.PENDING);
            } else {
                eventForUpdate.setState(EventStatus.CANCELED);
            }
        }
        if (eventDtoUserUpdate.getTitle() != null) eventForUpdate.setTitle(eventDtoUserUpdate.getTitle());
        return EventMapper.toEventDtoForResponse(eventRepository.save(eventForUpdate));
    }

    @Override
    public RequestDto canceledRequestByOwner(int userId, int requestId) {
        Request currentRequest = requestRepository.findByIdAndRegister(requestId, userId);
        currentRequest.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(currentRequest));
    }

    @Override
    public RequestDtoAfterChangeStatus requestDtoChangeStatus(int userId, int eventId, RequestDtoChangeStatus requestDtoChangeStatus) {
        Event currentEvent = eventRepository.findByIdAndInitiatorId(eventId, userId);
        int currentNumberOfConfirmedRequest = currentEvent.getConfirmedRequests();
        if (currentEvent.getParticipantLimit() == currentNumberOfConfirmedRequest) {
            throw new IllegalArgumentException("The participant limit has been reached");
        }
        List<Specification<Request>> specifications = requestFilterToSpecification(requestDtoChangeStatus.getRequestIds());
        List<Request> requestsForChangeStatus = requestRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null));
        if (requestDtoChangeStatus.getStatus().equals(RequestStatus.CONFIRMED)) {
            for (Request currentRequest : requestsForChangeStatus) {
                if (currentEvent.getParticipantLimit() > currentNumberOfConfirmedRequest) {
                    if (currentRequest.getStatus().equals(RequestStatus.PENDING)) {
                        currentRequest.setStatus(RequestStatus.CONFIRMED);
                        currentNumberOfConfirmedRequest++;
                    } else {
                        throw new IllegalArgumentException("Request must have status PENDING");
                    }
                } else {
                    if (currentRequest.getStatus().equals(RequestStatus.PENDING)) {
                        currentRequest.setStatus(RequestStatus.REJECTED);
                    } else {
                        throw new IllegalArgumentException("Request must have status PENDING");
                    }
                }
            }
        }
        if (requestDtoChangeStatus.getStatus().equals(RequestStatus.REJECTED)) {
            for (Request currentRequest : requestsForChangeStatus) {
                if (currentRequest.getStatus().equals(RequestStatus.PENDING)) {
                    currentRequest.setStatus(RequestStatus.REJECTED);
                } else {
                    throw new IllegalArgumentException("Request must have status PENDING");
                }
            }
        }
        currentEvent.setConfirmedRequests(currentNumberOfConfirmedRequest);
        eventRepository.save(currentEvent);
        List<RequestDto> requestDtoAfterSave = requestRepository.saveAll(requestsForChangeStatus).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
        RequestDtoAfterChangeStatus requestDtoAfterChangeStatus = new RequestDtoAfterChangeStatus();
        requestDtoAfterSave.forEach(requestDto -> {
            if (requestDto.getStatus().equals(RequestStatus.CONFIRMED)) {
                requestDtoAfterChangeStatus.getConfirmedRequests().add(requestDto);
            } else {
                requestDtoAfterChangeStatus.getRejectedRequests().add(requestDto);
            }
        });
        return requestDtoAfterChangeStatus;
    }

    @Override
    public List<EventDtoForShortResponse> getEventsByInitiatorId(int userId, int from, int size) {
        int currentPage = from / size;
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        List<Integer> allEventsId = events.stream().map(Event::getId).collect(Collectors.toList());
        List<Specification<Request>> specifications = requestFilterToSpecification(allEventsId);
        List<Request> requestsByEventsId = requestRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null));
        List<EventDtoForShortResponse> responses = new ArrayList<>();

        for (Event event : events) {
            int confirmedRequests = (int) requestsByEventsId.stream().filter(r -> event.getId() == r.getEvent())
                    .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                    .count();

            responses.add(EventMapper.toEventDtoForShortResponse(event));
        }
        return responses;
    }

    @Override
    public EventDtoForResponse getEventsById(int userId, int eventId) {
        Event currentEvent = eventRepository.findByIdAndInitiatorId(eventId, userId);
        int numberOfConfirmedRequest = requestRepository.findByEventAndStatus(eventId, RequestStatus.CONFIRMED).size();
        EventDtoForResponse response = EventMapper.toEventDtoForResponse(currentEvent);
        response.setConfirmedRequests(numberOfConfirmedRequest);
        return response;
    }

    @Override
    public List<RequestDto> getRequestsByEventId(int userId, int eventId) {
        Event currentEvent = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return requestRepository.findByEvent(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestsByRegisterId(int userId) {
        return requestRepository.findByRegister(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(int userId, int eventId, CommentDtoCreate commentDto) {
        if (!eventRepository.getReferenceById(eventId).getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalArgumentException("Event not found");
        }
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(userId, eventId, commentDto)));
    }

    @Override
    public CommentDto updateComment(int userId, int commentId, CommentDtoCreate commentDto) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if (comment.getText() == null) {
            throw new IllegalArgumentException("Comment not found");
        }
        if (comment.getAuthor() != userId) {
            throw new IllegalArgumentException("This comment does not belong to the user");
        }
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void removeComment(int userId, int commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if (comment.getAuthor() != userId) {
            throw new IllegalArgumentException("This comment does not belong to the user");
        }
        try {
            commentRepository.delete(comment);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private List<Specification<Request>> requestFilterToSpecification(List<Integer> ids) {
        List<Specification<Request>> specifications = new ArrayList<>();
        specifications.add(idIn(ids));
        return specifications;
    }

    private Specification<Request> idIn(List<Integer> values) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(values));
    }
}
