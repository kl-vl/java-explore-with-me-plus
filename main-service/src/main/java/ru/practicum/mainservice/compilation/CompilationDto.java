package ru.practicum.mainservice.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ru.practicum.mainservice.event.EventDto;

import java.util.List;

public class CompilationDto {

    Long id;
    List<EventDto> events;
    Boolean pinned;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    String title;

}
