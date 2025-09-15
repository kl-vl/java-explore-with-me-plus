package ru.practicum.mainservice.event;

import ru.practicum.mainservice.category.CategoryDto;
import ru.practicum.mainservice.user.UserDto;

import java.time.LocalDateTime;

public class EventDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserDto initiator;
    // TODO
    //LocationDto location;
    Boolean paid;
    Integer participantLimit;
    // TODO
    LocalDateTime publishedOn;
    Boolean requestModeration;
    EventState state;
    String title;
    Long views;

}
