package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserSearchFilter;
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
    public List<UserDto> getUsers(UserSearchFilter userSearchFilter) {
        log.info("Выполняется запрос на получение списка пользователей по параметрам запроса: ids: {}; from: {}; size: {}",
                userSearchFilter.getIds(),
                userSearchFilter.getFrom(),
                userSearchFilter.getSize());
        return administratorService.getUsers(userSearchFilter);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity removeUser(@PathVariable int userId) {
        log.info("Запрос на удаление пользователя под id: {}", userId);
        administratorService.removeUser(userId);
        return new ResponseEntity(null, null, HttpStatus.NO_CONTENT);
    }
}
