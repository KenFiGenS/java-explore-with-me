package ru.practicum.service.administrator;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.CompilationDtoForCreate;
import ru.practicum.dto.compilation.ComplicationDtoForResponse;
import ru.practicum.dto.event.EventDtoAdminUpdate;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.SearchFilterForAdmin;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdministratorService {
    UserDto createUser(UserDto userDtoCreate);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    void removeUser(int id);

    CategoryDto createCategory(CategoryDto categoryDto);

    void removeCategory(int catId);

    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    EventDtoForResponse updateEvent(int eventId, EventDtoAdminUpdate eventDtoAdminUpdate);

    List<EventDtoForResponse> getAllEventBySpecification(SearchFilterForAdmin specificationDtoForFindEvent, int from, int size);

    ComplicationDtoForResponse createCompilation(CompilationDtoForCreate dtoForCreate);

    ComplicationDtoForResponse updateCompilation(int compId, CompilationDtoForCreate dtoForCreate);
}
