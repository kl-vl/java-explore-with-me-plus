package ru.practicum.mainservice.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.mainservice.event.dto.EventDto;

import java.util.Set;

@Builder
@Getter
@AllArgsConstructor
public class CompilationDto {
    @Null
    private Long id;
    private Set<EventDto> events;
    @NotNull
    private Boolean pinned;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

}
