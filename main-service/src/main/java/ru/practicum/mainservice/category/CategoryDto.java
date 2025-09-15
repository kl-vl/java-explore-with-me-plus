package ru.practicum.mainservice.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoryDto {
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

}
