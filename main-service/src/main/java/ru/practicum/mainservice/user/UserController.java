package ru.practicum.mainservice.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.dto.UserSave;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero final Integer from,
            @RequestParam(defaultValue = "10") @PositiveOrZero final Integer size,
            @RequestParam(required = false) final List<Integer> ids
    ) {
        return userService.findAll(from, size, ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody @Valid final UserSave userSave) {
        return userService.create(userSave);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDto delete(@PathVariable @PositiveOrZero final Long userId) {
        return userService.delete(userId);
    }
}

