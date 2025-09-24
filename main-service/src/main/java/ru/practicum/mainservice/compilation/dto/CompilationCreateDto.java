package ru.practicum.mainservice.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.mainservice.validation.ValidationGroups;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CompilationCreateDto {
    private Set<Long> events;

    private Boolean pinned;

    @NotNull(groups = ValidationGroups.Create.class)
    @NotBlank(groups = {ValidationGroups.Create.class})
    @Size(min = 1, max = 50, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String title;

}
