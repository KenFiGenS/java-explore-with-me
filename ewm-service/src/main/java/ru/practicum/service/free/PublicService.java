package ru.practicum.service.free;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface PublicService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoriesById(int catId);
}
