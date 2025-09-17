package ru.practicum.mainservice.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.mainservice.validation.ValidationGroups;

import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
public class CompilationCreateDto {
    private Set<Long> events;

    @NotNull(groups = ValidationGroups.Create.class)
    private Boolean pinned;

    @NotNull(groups = ValidationGroups.Create.class)
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

}
