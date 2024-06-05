package ru.practicum.service.administrator;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CategoryMapper;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.Category;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.UserRepository;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public UserDto createUser(UserDto userDtoCreate) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDtoCreate)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        if (!ids.isEmpty()) {
            List<Specification<User>> specifications = userFilterToSpecification(ids);
            return userRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            int currentPage = from / size;
            Pageable pageable = PageRequest.of(currentPage, size);
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @SneakyThrows
    @Override
    public void removeUser(int id) {
        try {
            User currentUser = userRepository.getReferenceById(id);
            userRepository.delete(currentUser);
        } catch (RuntimeException e) {
            throw new SQLDataException(e.getMessage());
        }
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @SneakyThrows
    @Override
    public void removeCategory(int catId) {
        try {
            Category currentCategory = categoryRepository.getReferenceById(catId);
            categoryRepository.delete(currentCategory);
        } catch (RuntimeException e) {
            throw new SQLDataException(e.getMessage());
        }
    }

    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        categoryDto.setId(catId);
        Category currentCategory = categoryRepository.getReferenceById(catId);
        if(currentCategory.getName().equals(categoryDto.getName())) return categoryDto;
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    private List<Specification<User>> userFilterToSpecification(List<Integer> ids) {
        List<Specification<User>> specifications = new ArrayList<>();
        specifications.add(idIn(ids));
        return specifications;
    }

    private Specification<User> idIn(List<Integer> values) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(values));
    }
}
