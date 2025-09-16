package ru.practicum.mainservice.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.dto.UserSave;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll(Integer from, Integer size, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(from, size);
        } else {
            return userRepository.findAllById(ids);
        }
    }

    public UserDto create(UserSave userSave) {
        UserDto userDtoSave = UserDto.builder()
                .email(userSave.getEmail())
                .name(userSave.getName())
                .build();
        return userRepository.save(userDtoSave);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
