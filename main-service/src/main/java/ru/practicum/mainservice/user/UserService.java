package ru.practicum.mainservice.user;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    void deleteUserById(Long userId);

    List<UserDto> findAllUsers(Integer from, Integer size, List<Long> ids);
}
