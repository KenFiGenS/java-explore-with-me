package ru.practicum.service.administrator;

import org.springframework.http.HttpStatus;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserSearchFilter;

import java.util.List;

public interface AdministratorService {
    UserDto createUser(UserDto userDtoCreate);

    List<UserDto> getUsers(UserSearchFilter userSearchFilter);

    void removeUser(int id);
}
