package ru.practicum.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.EventStatus;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RequestRepository requestRepository;

    @Override
    public EventDtoForResponse createEvent(int userId, EventDtoCreate eventDtoCreate) {
        if (eventDtoCreate.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Дата начала события менее чем через 2 часа");
        }
        Category currentCategory = categoryRepository.getReferenceById(eventDtoCreate.getCategory());
        User initiator = userRepository.getReferenceById(userId);
        Event currentEvent = EventMapper.toEvent(eventDtoCreate, currentCategory, initiator);
        Event currentEventAfterSave = eventRepository.save(currentEvent);
        return EventMapper.toEventDtoAfterCreate(currentEventAfterSave);
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
        if (currentEvent.getParticipantLimit() < requestsByEventId.size() + 1) {
            throw new IllegalArgumentException("The event gathered the maximum number of participants");
        }
        return RequestMapper.toRequestDto(requestRepository.save(RequestMapper.toRequest(userId, eventId)));
    }

    @Override
    public EventDtoForResponse updateRequest(int userId, int eventId, EventDtoUserUpdate eventDtoUserUpdate) {
        Event eventForUpdate = eventRepository.getReferenceById(eventId);
        if (eventForUpdate.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalArgumentException("Cannot update the event because it's not in the right state: PUBLISHED");
        }
        if (eventDtoUserUpdate.getAnnotation() != null) eventForUpdate.setAnnotation(eventDtoUserUpdate.getAnnotation());
        if (eventDtoUserUpdate.getCategory() != 0) {
            Category categoryForUpdate = categoryRepository.getReferenceById(eventDtoUserUpdate.getCategory());
            eventForUpdate.setCategory(categoryForUpdate);
        }
        if (eventDtoUserUpdate.getDescription() != null)eventForUpdate.setDescription(eventDtoUserUpdate.getDescription());
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
        if (eventDtoUserUpdate.getPaid() != null)eventForUpdate.setPaid(eventDtoUserUpdate.getPaid());
        if (eventDtoUserUpdate.getParticipantLimit() > 0)eventForUpdate.setParticipantLimit(eventDtoUserUpdate.getParticipantLimit());
        if (eventDtoUserUpdate.getRequestModeration() != null)eventForUpdate.setRequestModeration(eventDtoUserUpdate.getRequestModeration());
        if (eventDtoUserUpdate.getStateAction() != null) {
            if (eventDtoUserUpdate.getStateAction().equals(StateActionForUser.SEND_TO_REVIEW)) {
                eventForUpdate.setState(EventStatus.PENDING);
            } else {
                eventForUpdate.setState(EventStatus.CANCELED);
            }
        }
        if (eventDtoUserUpdate.getTitle() != null)eventForUpdate.setTitle(eventDtoUserUpdate.getTitle());
        return EventMapper.toEventDtoAfterCreate(eventRepository.save(eventForUpdate));
    }
}
