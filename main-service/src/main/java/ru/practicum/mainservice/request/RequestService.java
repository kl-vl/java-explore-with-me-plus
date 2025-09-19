package ru.practicum.mainservice.request;

import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.ParticipantLimitExceededException;
import ru.practicum.mainservice.exception.RequestAlreadyExistsException;
import ru.practicum.mainservice.exception.RequestNotFoundException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.request.dto.RequestDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateResultDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId) throws EventNotFoundException;

    RequestStatusUpdateResultDto updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto) throws EventNotFoundException;

    RequestDto createRequest(Long userId, Long eventId) throws UserNotFoundException, EventNotFoundException, RequestAlreadyExistsException, ParticipantLimitExceededException;

    List<RequestDto> getCurrentUserRequests(Long userId) throws UserNotFoundException;

    RequestDto cancelRequests(Long userId, Long requestId) throws RequestNotFoundException;

}
