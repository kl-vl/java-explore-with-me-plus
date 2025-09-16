package ru.practicum.mainservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.mainservice.user.User;

/**
 * DTO for {@link User}
 */
@AllArgsConstructor
@Getter
public class UserSave {

    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private final String email;

    @NotBlank
    @Size(min = 2, max = 250)
    private final String name;
}