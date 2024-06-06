package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDtoAfterCreate;
import ru.practicum.dto.event.EventDtoCreate;
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
    public ResponseEntity<EventDtoAfterCreate> createEvent(@PathVariable int userId, @Validated @RequestBody EventDtoCreate eventDtoCreate) {
        log.info("Запрос на создание события: {}", eventDtoCreate.getTitle());
        return new ResponseEntity<>(userService.createEvent(userId, eventDtoCreate), null, HttpStatus.CREATED);
    }

}
