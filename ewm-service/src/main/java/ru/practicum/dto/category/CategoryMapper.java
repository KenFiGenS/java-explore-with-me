package ru.practicum.dto.category;

import lombok.experimental.UtilityClass;
import ru.practicum.model.category.Category;


@UtilityClass
public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(0, categoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return  new CategoryDto(category.getId(), category.getName());
    }
}
