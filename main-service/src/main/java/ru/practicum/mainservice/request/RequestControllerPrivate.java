package ru.practicum.mainservice.request;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventNotPublishedException;
import ru.practicum.mainservice.exception.ParticipantLimitExceededException;
import ru.practicum.mainservice.exception.RequestAlreadyExistsException;
import ru.practicum.mainservice.exception.RequestNotFoundException;
import ru.practicum.mainservice.exception.RequestSelfAttendException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.request.dto.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestControllerPrivate {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getParticipationRequest(@PathVariable @Positive Long userId) throws UserNotFoundException {
        return requestService.getCurrentUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto participationRequest(@PathVariable @Positive Long userId, @RequestParam @Positive Long eventId) throws UserNotFoundException, ParticipantLimitExceededException, EventNotFoundException, RequestAlreadyExistsException, RequestSelfAttendException, EventNotPublishedException {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelParticipationRequest(@PathVariable @Positive Long userId, @PathVariable @Positive Long requestId) throws RequestNotFoundException {
        return requestService.cancelRequests(userId, requestId);
    }

}
