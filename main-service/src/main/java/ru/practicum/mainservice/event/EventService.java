package ru.practicum.mainservice.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventDtoFull;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.dto.EventFilterPublic;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.EventAlreadyPublishedException;
import ru.practicum.mainservice.exception.EventCanceledCantPublishException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventValidationException;
import ru.practicum.mainservice.exception.EventDateException;
import ru.practicum.mainservice.exception.FilterValidationException;
import ru.practicum.mainservice.exception.UserNotFoundException;

import java.util.List;

public interface EventService {
    // Admin
    List<EventDtoFull> findEventsByUsers(EventFilterAdmin eventFilter) throws FilterValidationException, EventDateException;

    EventDtoFull updateEventById(EventDto eventDto) throws EventNotFoundException, EventValidationException, EventDateException, EventAlreadyPublishedException, EventCanceledCantPublishException;

    // Private
    EventDtoFull createEvent(EventDto eventDto) throws EventValidationException, CategoryNotFoundException, UserNotFoundException, EventDateException;

    List<EventDtoFull> findEventsByUserid(Long userId, int from, int size);

    EventDtoFull findEventByUserId(Long userId, Long eventId) throws EventNotFoundException;

    EventDtoFull updateEventByUserId(EventDto eventDto) throws EventNotFoundException, EventDateException, EventCanceledCantPublishException;

    //Public
    List<EventDtoFull> findEvents(EventFilterPublic eventFilter, HttpServletRequest request) throws FilterValidationException, EventDateException;

    EventDtoFull findEventById(Long eventId, HttpServletRequest request) throws EventNotFoundException;

}
