package ru.practicum.mainservice.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    public interface Create {
    }

    public interface Update {
    }

    @Null(groups = CategoryDto.Create.class)
    private Long id;

    @NotNull(groups = CategoryDto.Create.class)
    @NotBlank(groups = {Create.class, Update.class})
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    private String name;

}
