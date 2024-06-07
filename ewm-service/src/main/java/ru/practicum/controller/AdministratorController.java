package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventDtoAdminUpdate;
import ru.practicum.dto.event.EventDtoAfterCreate;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.administrator.AdministratorService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(path = "/admin")
public class AdministratorController {
    @Autowired
    AdministratorService administratorService;

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody UserDto userDtoCreate) {
        log.info("Выполняется запрос на создание пользователя. Email: {}, Name: {}", userDtoCreate.getEmail(), userDtoCreate.getName());
        return new ResponseEntity<>(administratorService.createUser(userDtoCreate), null, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") List<Integer> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Выполняется запрос на получение списка пользователей по параметрам запроса: ids: {}; from: {}; size: {}", ids, from, size);
        return administratorService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity removeUser(@PathVariable int userId) {
        log.info("Запрос на удаление пользователя под id: {}", userId);
        administratorService.removeUser(userId);
        return new ResponseEntity(null, null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Validated @RequestBody CategoryDto categoryDto) {
        log.info("Выполняется запрос на создание категории под названием: {}", categoryDto.getName());
        return new ResponseEntity<>(administratorService.createCategory(categoryDto), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity removeCategory(@PathVariable int catId) {
        log.info("Запрос на удаление категории под id: {}", catId);
        administratorService.removeCategory(catId);
        return new ResponseEntity(null, null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Validated @RequestBody CategoryDto categoryDto,
                                      @PathVariable int catId) {
        log.info("Выполняется запрос на обновление категории под id: {}, новое название: {}", catId, categoryDto.getName());
        return administratorService.updateCategory(catId, categoryDto);
    }

    @PatchMapping("/events/{eventId}")
    public EventDtoAfterCreate updateEvent(@RequestBody EventDtoAdminUpdate eventDtoAdminUpdate,
                                                           @PathVariable int eventId) {
        log.info("Выполняется запрос на обновление события под id: {}", eventId);
        EventDtoAfterCreate eventDtoAfterUpdate = administratorService.updateEvent(eventId, eventDtoAdminUpdate);
        return eventDtoAfterUpdate;
    }
}
