package ru.practicum.mainservice.user;

import ru.practicum.mainservice.exception.UserAlreadyExistsException;
import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    void deleteUserById(Long userId);

    List<UserDto> findAllUsers(Integer from, Integer size, List<Long> ids);
}
