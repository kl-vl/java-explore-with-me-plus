package ru.practicum.mainservice.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.UserAlreadyExistsException;
import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAllUsers(Integer from, Integer size, List<Long> ids) {
        log.info("Main-service. findAll input: from = {}, size = {}, ids = {}", from, size, ids);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<Long> idsForQuery = (ids == null || ids.isEmpty()) ? null : ids;
        Page<User> page = userRepository.findAllByIds(idsForQuery, pageable);

        log.info("Main-service. findAll success: found {} users", page.getNumberOfElements());

        return page.getContent().stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) throws UserAlreadyExistsException {
        log.info("Main-service. createUser input: email = {}", userDto.getEmail());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists");
        }

        User createdUser = userRepository.save(userMapper.toEntity(userDto));

        log.info("Main-service. createUser success: id = {}", createdUser.getId());

        return userMapper.toUserDto(createdUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        log.info("Main-service. deleteUserById input: userId = {}", userId);

        userRepository.deleteById(userId);

        log.info("Main-service. deleteUserById success");
    }
}
