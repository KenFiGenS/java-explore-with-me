package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.EventDtoCreate;
import ru.practicum.dto.event.EventDtoUserUpdate;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.service.user.UserService;

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

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(@PathVariable int userId, @RequestParam int eventId) {
        log.info("Запрос на участие пользователя под id {} в событии под id: {}", userId, eventId);
        return new ResponseEntity<>(userService.createRequest(userId, eventId), null, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDtoForResponse updateRequest(@PathVariable int userId,
                                             @PathVariable int eventId,
                                             @RequestBody EventDtoUserUpdate eventDtoUserUpdate) {
        log.info("Запрос на обновление события под id: {}, от пользователя под id {}", eventId, userId);
        return userService.updateRequest(userId, eventId, eventDtoUserUpdate);
    }
}
