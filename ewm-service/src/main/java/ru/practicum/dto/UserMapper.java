package ru.practicum.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.model.User;

@UtilityClass
public class UserMapper {

    public static UserDtoCreate toUserDto(User user) {
        return new UserDtoCreate(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static User toUser(UserDtoCreate userDto) {
        return new User(
                userDto.getId(),
                userDto.getEmail(),
                userDto.getName()
        );
    }
}
