package ru.practicum.mainservice.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainservice.event.enums.EventStateAction;
import ru.practicum.mainservice.location.LocationDto;
import ru.practicum.mainservice.validation.ValidationGroups;

import java.time.LocalDateTime;

/**
 * EvenDto входная для контролллера
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @Null(groups = ValidationGroups.Create.class)
    private Long id;

    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(groups = ValidationGroups.Create.class)
    @Positive
    private Long category;

    @NotBlank(groups = ValidationGroups.Create.class)
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(groups = ValidationGroups.Create.class)
    private LocalDateTime eventDate;

    private Long initiator;

    @NotNull(groups = ValidationGroups.Create.class)
    LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStateAction stateAction;

    @NotNull(groups = ValidationGroups.Create.class)
    @Size(min = 3, max = 120)
    private String title;

    private Long views;

}
