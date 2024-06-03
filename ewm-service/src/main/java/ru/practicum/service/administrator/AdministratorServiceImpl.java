package ru.practicum.service.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserDtoCreate;
import ru.practicum.dto.UserMapper;
import ru.practicum.repository.UserRepository;

@Service
public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDtoCreate createUser(UserDtoCreate userDtoCreate) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDtoCreate)));
    }
}
