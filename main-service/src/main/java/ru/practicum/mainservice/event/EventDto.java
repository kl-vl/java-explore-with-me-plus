package ru.practicum.mainservice.event;

import ru.practicum.mainservice.category.CategoryDto;
import ru.practicum.mainservice.user.UserDto;

import java.time.LocalDateTime;

public class EventDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserDto initiator;
    // TODO
    //LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    // TODO
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;

}
