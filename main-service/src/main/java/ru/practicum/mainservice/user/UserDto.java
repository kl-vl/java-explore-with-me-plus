package ru.practicum.mainservice.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserDto {
    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = UserDto.Create.class)
    private Long id;

    @Email(groups = {UserDto.Create.class, UserDto.Update.class})
    @NotBlank(groups = {UserDto.Create.class, UserDto.Update.class})
    @Size(min = 6, max = 254, groups = {UserDto.Create.class, UserDto.Update.class})
    private String email;

    @NotNull(groups = UserDto.Create.class)
    @NotBlank(groups = {UserDto.Create.class, UserDto.Update.class})
    @Size(min = 2, max = 250,  groups = {Create.class, Update.class})
    private String name;

}
