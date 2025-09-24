package ru.practicum.mainservice.event.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.event.EventService;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventDtoFull;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.EventCanceledCantPublishException;
import ru.practicum.mainservice.exception.EventDateException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventNotPublishedException;
import ru.practicum.mainservice.exception.EventValidationException;
import ru.practicum.mainservice.exception.ParticipantLimitExceededException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.request.RequestService;
import ru.practicum.mainservice.request.dto.RequestDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateResultDto;
import ru.practicum.mainservice.validation.ValidationGroups;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {

    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoFull createEvent(@PathVariable @Positive Long userId, @RequestBody @Validated({ValidationGroups.Create.class, Default.class}) EventDto eventDto) throws UserNotFoundException, CategoryNotFoundException, EventDateException, EventValidationException {
        eventDto.setInitiator(userId);
        return eventService.createEvent(eventDto);
    }

    @GetMapping
    public List<EventDtoFull> findEventsByUser(@PathVariable @Positive Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {

        return eventService.findEventsByUserid(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDtoFull findEventByUserId(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId) throws EventNotFoundException {

        return eventService.findEventByUserId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDtoFull updateEventByUserId(@PathVariable Long userId, @PathVariable Long eventId,
                                            @RequestBody @Validated(Default.class) EventDto eventDto) throws EventNotFoundException, EventDateException, EventCanceledCantPublishException {
        eventDto.setInitiator(userId);
        eventDto.setId(eventId);
        return eventService.updateEventByUserId(eventDto);
    }

    /**
     *  Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByOwnerOfEvent(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId) throws EventNotFoundException {
        return requestService.getRequestsByOwnerOfEvent(userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResultDto updateRequests(@PathVariable @Positive Long userId,
                                                       @PathVariable @Positive Long eventId,
                                                       @RequestBody @Validated RequestStatusUpdateDto requestStatusUpdateDto) throws EventNotFoundException, EventNotPublishedException, ParticipantLimitExceededException {
        return requestService.updateRequests(userId, eventId, requestStatusUpdateDto);
    }

}
