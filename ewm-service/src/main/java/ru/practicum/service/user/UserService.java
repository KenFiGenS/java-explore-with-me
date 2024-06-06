package ru.practicum.service.user;

import ru.practicum.dto.event.EventDtoAfterCreate;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.model.event.Event;

public interface UserService {
    EventDtoAfterCreate createEvent(int userId, EventDtoCreate eventDtoCreate);
}
