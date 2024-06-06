package ru.practicum.service.free;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicServiceImpl implements PublicService{
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        int currentPage = from / size;
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (categories.isEmpty()) {
            return Arrays.asList();
        }
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoriesById(int catId) {
        return CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(catId));
    }
}
