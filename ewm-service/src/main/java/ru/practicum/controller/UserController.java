package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventDtoForShortResponse;
import ru.practicum.dto.event.EventDtoUserUpdate;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestDtoAfterChangeStatus;
import ru.practicum.dto.request.RequestDtoChangeStatus;
import ru.practicum.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventDtoForResponse> createEvent(@PathVariable int userId, @Validated @RequestBody EventDtoCreate eventDtoCreate) {
        log.info("Запрос на создание события: {}", eventDtoCreate.getTitle());
        return new ResponseEntity<>(userService.createEvent(userId, eventDtoCreate), null, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDtoForResponse updateRequest(@PathVariable int userId,
                                             @PathVariable int eventId,
                                             @Validated @RequestBody EventDtoUserUpdate eventDtoUserUpdate) {
        log.info("Запрос на обновление события под id: {}, от пользователя под id {}", eventId, userId);
        return userService.updateRequest(userId, eventId, eventDtoUserUpdate);
    }

    @GetMapping("/{userId}/events")
    public List<EventDtoForShortResponse> getEventsByInitiatorId(@PathVariable int userId,
                                                                 @RequestParam(defaultValue = "0") int from,
                                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Запрос на получение списка событий созданных пользователем под id: {}", userId);
        return userService.getEventsByInitiatorId(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDtoForResponse getEventsById(@PathVariable int userId,
                                             @PathVariable int eventId) {
        log.info("Запрос на получение события под id:{}, созданного пользователем под id: {}", eventId, userId);
        return userService.getEventsById(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequestsByEventId(@PathVariable int userId,
                                                 @PathVariable int eventId) {
        log.info("Запрос на получение списка запросов на участие в событии под id: {} созданном пользователем под id: {}", eventId, userId);
        return userService.getRequestsByEventId(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<RequestDto> getRequestsByRegisterId(@PathVariable int userId) {
        log.info("Запрос на получение списка запросов на участие в событиях пользователя под id: {}", userId);
        return userService.getRequestsByRegisterId(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto canceledRequestByOwner(@PathVariable int userId,
                                             @PathVariable int requestId) {
        log.info("Запрос на отмену своего запроса id: {} на участие в событии от пользователя под id {}", requestId, userId);
        return userService.canceledRequestByOwner(userId, requestId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(@PathVariable int userId, @RequestParam int eventId) {
        log.info("Запрос на участие пользователя под id {} в событии под id: {}", userId, eventId);
        return new ResponseEntity<>(userService.createRequest(userId, eventId), null, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestDtoAfterChangeStatus requestChangeStatusByEventOwner(@PathVariable int userId,
                                                                       @PathVariable int eventId,
                                                                       @RequestBody RequestDtoChangeStatus requestDtoChangeStatus) {
        log.info("Запрос на изменение статуса заявок на участие в событии под id: {}, от организатора под id {}", eventId, userId);
        return userService.requestDtoChangeStatus(userId, eventId, requestDtoChangeStatus);
    }
}
