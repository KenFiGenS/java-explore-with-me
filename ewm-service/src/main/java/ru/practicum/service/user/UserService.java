package ru.practicum.service.user;

import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventDtoUserUpdate;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestDtoAfterChangeStatus;
import ru.practicum.dto.request.RequestDtoChangeStatus;

import java.util.List;

public interface UserService {
    EventDtoForResponse createEvent(int userId, EventDtoCreate eventDtoCreate);

    RequestDto createRequest(int userId, int eventId);

    EventDtoForResponse updateRequest(int userId, int eventId, EventDtoUserUpdate eventDtoUserUpdate);

    RequestDto canceledRequestByOwner(int userId, int requestId);

    RequestDtoAfterChangeStatus requestDtoChangeStatus(int userId, int eventId, RequestDtoChangeStatus requestDtoChangeStatus);
}
