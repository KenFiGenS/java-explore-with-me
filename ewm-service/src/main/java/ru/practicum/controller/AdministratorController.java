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
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.SearchFilterForAdmin;
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
        log.info("ADMIN: Выполняется запрос на создание пользователя. Email: {}, Name: {}", userDtoCreate.getEmail(), userDtoCreate.getName());
        return new ResponseEntity<>(administratorService.createUser(userDtoCreate), null, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(defaultValue = "") List<Integer> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("ADMIN: Выполняется запрос на получение списка пользователей по параметрам запроса: ids: {}; from: {}; size: {}", ids, from, size);
        return administratorService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity removeUser(@PathVariable int userId) {
        log.info("ADMIN: Запрос на удаление пользователя под id: {}", userId);
        administratorService.removeUser(userId);
        return new ResponseEntity(null, null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Validated @RequestBody CategoryDto categoryDto) {
        log.info("ADMIN: Выполняется запрос на создание категории под названием: {}", categoryDto.getName());
        return new ResponseEntity<>(administratorService.createCategory(categoryDto), null, HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity removeCategory(@PathVariable int catId) {
        log.info("ADMIN: Запрос на удаление категории под id: {}", catId);
        administratorService.removeCategory(catId);
        return new ResponseEntity(null, null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@Validated @RequestBody CategoryDto categoryDto,
                                      @PathVariable int catId) {
        log.info("ADMIN: Выполняется запрос на обновление категории под id: {}, новое название: {}", catId, categoryDto.getName());
        return administratorService.updateCategory(catId, categoryDto);
    }

    @PatchMapping("/events/{eventId}")
    public EventDtoForResponse updateEvent(@Validated @RequestBody EventDtoAdminUpdate eventDtoAdminUpdate,
                                           @PathVariable int eventId) {
        log.info("ADMIN: Выполняется запрос на обновление события под id: {}", eventId);
        EventDtoForResponse eventDtoAfterUpdate = administratorService.updateEvent(eventId, eventDtoAdminUpdate);
        return eventDtoAfterUpdate;
    }

    @GetMapping("/events")
    public List<EventDtoForResponse> getAllEventBySpecification(SearchFilterForAdmin searchFilterForAdmin,
                                                                @RequestParam(defaultValue = "0") int from,
                                                                @RequestParam(defaultValue = "10") int size) {
        log.info("ADMIN: Запрос на получение списка событий по спецификации: {}", searchFilterForAdmin);
        return administratorService.getAllEventBySpecification(searchFilterForAdmin, from, size);
    }
}
