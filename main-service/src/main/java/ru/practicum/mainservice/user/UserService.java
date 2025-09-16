package ru.practicum.mainservice.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.dto.UserSave;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll(final Integer from, final Integer size, final List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(from, size);
        } else {
            return userRepository.findManyById(ids);
        }
    }

    public User create(final UserSave userSave) {
        User user = User.builder()
                .email(userSave.getEmail())
                .name(userSave.getName())
                .build();
        return userRepository.save(user);
    }

    public UserDto delete(final Long id) {
        Optional<UserDto> user = userRepository.findOne(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return user.get();
        }
        return null;
    }
}
