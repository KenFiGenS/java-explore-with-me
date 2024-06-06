package ru.practicum.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventDtoAfterCreate;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

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
    public EventDtoAfterCreate createEvent(int userId, EventDtoCreate eventDtoCreate) {
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
    public RequestDto createRequest(int userId, int requestId) {
        return RequestMapper.toRequestDto(requestRepository.save(RequestMapper.toRequest(userId, requestId)));
    }
}
