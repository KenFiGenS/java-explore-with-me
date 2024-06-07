package ru.practicum.service.user;

import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventDtoUserUpdate;
import ru.practicum.dto.request.RequestDto;

public interface UserService {
    EventDtoForResponse createEvent(int userId, EventDtoCreate eventDtoCreate);

    RequestDto createRequest(int userId, int eventId);

    EventDtoForResponse updateRequest(int userId, int eventId, EventDtoUserUpdate eventDtoUserUpdate);
}
