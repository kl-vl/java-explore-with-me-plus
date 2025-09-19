package ru.practicum.mainservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.mainservice.validation.ValidationGroups;

@AllArgsConstructor
@Builder
@Getter
public class UserDto {

    @Null(groups = ValidationGroups.Create.class)
    private Long id;

    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;

    @NotNull(groups = ValidationGroups.Create.class)
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

}
