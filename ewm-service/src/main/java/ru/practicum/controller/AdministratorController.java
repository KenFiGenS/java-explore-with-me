package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.UserDtoCreate;
import ru.practicum.service.administrator.AdministratorService;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin")
public class AdministratorController {
    @Autowired
    AdministratorService administratorService;

    @PostMapping("/users")
    public UserDtoCreate createUser(@Valid @RequestBody UserDtoCreate userDtoCreate) {
        log.info("Выполняется запрос на создание пользователя. Email: {}, Name: {}", userDtoCreate.getEmail(), userDtoCreate.getName());
        return administratorService.createUser(userDtoCreate);
    }
}
