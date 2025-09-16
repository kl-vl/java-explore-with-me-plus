package ru.practicum.mainservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.category.CategoryDto;
import ru.practicum.mainservice.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoShort {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}