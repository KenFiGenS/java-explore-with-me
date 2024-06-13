package ru.practicum.service.free;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.compilation.ComplicationDtoForResponse;
import ru.practicum.dto.event.EventDtoForResponse;
import ru.practicum.dto.event.SearchFilterForPublic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoriesById(int catId);

    EventDtoForResponse getEventById(int id, HttpServletRequest request);

    List<EventDtoForResponse> getEventsBySearchFilter(SearchFilterForPublic filter, String sort, int from, int size, HttpServletRequest request);

    ComplicationDtoForResponse getCompilationById(int compId);

    List<ComplicationDtoForResponse> getCompilations(boolean pinned, int from, int size);
}
