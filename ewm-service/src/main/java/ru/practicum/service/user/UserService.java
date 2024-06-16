package ru.practicum.service.user;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentDtoCreate;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventDtoForShortResponse;
import ru.practicum.dto.event.EventDtoUserUpdate;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestDtoAfterChangeStatus;
import ru.practicum.dto.request.RequestDtoChangeStatus;

import java.util.List;

public interface UserService {
    EventDtoForResponse createEvent(int userId, EventDtoCreate eventDtoCreate);

    RequestDto createRequest(int userId, int eventId);

    EventDtoForResponse updateEvent(int userId, int eventId, EventDtoUserUpdate eventDtoUserUpdate);

    RequestDto canceledRequestByOwner(int userId, int requestId);

    RequestDtoAfterChangeStatus requestDtoChangeStatus(int userId, int eventId, RequestDtoChangeStatus requestDtoChangeStatus);

    List<EventDtoForShortResponse> getEventsByInitiatorId(int userId, int from, int size);

    EventDtoForResponse getEventsById(int userId, int eventId);

    List<RequestDto> getRequestsByEventId(int userId, int eventId);

    List<RequestDto> getRequestsByRegisterId(int userId);

    CommentDto createComment(int userId, int eventId, CommentDtoCreate commentDto);

    CommentDto updateComment(int userId, int commentId, CommentDtoCreate commentDto);

    void removeComment(int userId, int commentId);
}
