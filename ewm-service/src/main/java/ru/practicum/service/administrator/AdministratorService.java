package ru.practicum.service.administrator;

import ru.practicum.dto.UserDtoCreate;

public interface AdministratorService {
    UserDtoCreate createUser(UserDtoCreate userDtoCreate);
}
