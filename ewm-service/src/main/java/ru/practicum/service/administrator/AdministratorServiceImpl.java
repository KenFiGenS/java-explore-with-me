package ru.practicum.service.administrator;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserMapper;
import ru.practicum.dto.user.UserSearchFilter;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDtoCreate) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDtoCreate)));
    }

    @Override
    public List<UserDto> getUsers(UserSearchFilter userSearchFilter) {
        if (userSearchFilter.getIds() != null || !userSearchFilter.getIds().isEmpty()) {
            List<Specification<User>> specifications = userFilterToSpecification(userSearchFilter);
            return userRepository.findAll(specifications.stream().reduce(Specification::or).orElse(null)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            int currentPage = userSearchFilter.getFrom() / userSearchFilter.getSize();
            Pageable pageable = PageRequest.of(currentPage, userSearchFilter.getSize());
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
        } catch (Exception e) {
            throw new SQLDataException(e.getMessage());
        }


    }

    private List<Specification<User>> userFilterToSpecification(UserSearchFilter userSearchFilter) {
        List<Specification<User>> specifications = new ArrayList<>();
        specifications.add(idIn(userSearchFilter.getIds()));
        return specifications;
    }

    private Specification<User> idIn(List<Integer> values) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("id")).value(values));
    }
}
