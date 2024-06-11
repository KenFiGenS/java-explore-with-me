package ru.practicum.service.free;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.EventDtoForResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoriesById(int catId);

    EventDtoForResponse getEventById(int id, HttpServletRequest request);
}
