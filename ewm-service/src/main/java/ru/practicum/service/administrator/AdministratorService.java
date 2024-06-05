package ru.practicum.service.administrator;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdministratorService {
    UserDto createUser(UserDto userDtoCreate);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    void removeUser(int id);

    CategoryDto createCategory(CategoryDto categoryDto);

    void removeCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);
}
