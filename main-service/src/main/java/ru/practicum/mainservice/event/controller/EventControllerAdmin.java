package ru.practicum.mainservice.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.EventService;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventDtoFull;
import ru.practicum.mainservice.exception.EventAlreadyPublishedException;
import ru.practicum.mainservice.exception.EventCanceledCantPublishException;
import ru.practicum.mainservice.exception.EventDateException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventValidationException;
import ru.practicum.mainservice.exception.FilterValidationException;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping
    public List<EventDtoFull> findEvents(@Valid EventFilterAdmin eventFilterAdmin) throws FilterValidationException, EventDateException {
        return eventService.findEventsByUsers(eventFilterAdmin);
    }

    @PatchMapping("{eventId}")
    public EventDtoFull updateEventById(@PathVariable Long eventId,
                                    @RequestBody @Validated(Default.class) EventDto eventDto) throws EventValidationException, EventNotFoundException, EventDateException, EventAlreadyPublishedException, EventCanceledCantPublishException {
        eventDto.setId(eventId);
        return eventService.updateEventById(eventDto);
    }
}
