package ru.practicum.mainservice.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.event.EventRepository;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.ParticipantLimitExceededException;
import ru.practicum.mainservice.exception.RequestAlreadyExistsException;
import ru.practicum.mainservice.exception.RequestNotFoundException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.request.dto.RequestDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateDto;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateResultDto;
import ru.practicum.mainservice.user.User;
import ru.practicum.mainservice.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) throws UserNotFoundException, EventNotFoundException, RequestAlreadyExistsException, ParticipantLimitExceededException {
        log.info("Main-server. createRequest input: userId = {}, eventId = {}", userId, eventId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestAlreadyExistsException("Request already exists");
        }

//        if (event.getInitiator().getId().equals(userId)) {
//            throw new SelfRequestException("Cannot request to your own event");
//        }
//        if (event.getState() != EventState.PUBLISHED) {
//            throw new EventNotPublishedException("Event is not published");
//        }

        Long confirmedCount = requestRepository.countConfirmedRequests(eventId);
        if (event.getParticipantLimit() > 0 && confirmedCount >= event.getParticipantLimit()) {
            throw new ParticipantLimitExceededException("Participant limit exceeded");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .build();

        Request savedRequest = requestRepository.save(request);

        log.info("Main-server. createRequest success {}", request.getId());

        return requestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional
    public RequestStatusUpdateResultDto updateRequests(Long userId, Long eventId, RequestStatusUpdateDto requestStatusUpdateDto) throws EventNotFoundException {
        //        если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        //        нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
        //        статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        //        если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("Event not found or access denied"));

//        if (event.getState() != EventState.PUBLISHED) {
//            throw new EventNotPublishedException("Event is not published");
//        }


        return null;
    }

    @Override
    @Transactional
    public RequestDto cancelRequests(Long userId, Long requestId) throws RequestNotFoundException {
        log.info("Main-server. cancelRequests input: userId = {}, requestId = {}", userId, requestId);
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        request.setStatus(RequestStatus.CANCELED);
        Request updatedRequest = requestRepository.save(request);

        log.info("Main-server. cancelRequests success: id = {}", updatedRequest.getId());

        return requestMapper.toDto(updatedRequest);
    }

    @Override
    public List<RequestDto> getCurrentUserRequests(Long userId) throws UserNotFoundException {
        log.info("Main-server. getCurrentUserRequests input: userId = {}", userId);

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Request> requests = requestRepository.findByRequesterId(userId);

        log.info("Main-server. getCurrentUserRequests success: size = {}", requests.size());

        return requestMapper.toDtoList(requests);
    }

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId) throws EventNotFoundException {
        log.info("Main-server. getRequestsByOwnerOfEvent input: userId = {}, eventId = {}", userId, eventId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException("Event not found or access denied"));

        List<Request> requests = requestRepository.findByEventIdAndEvent_InitiatorId(eventId, userId);

        log.info("Main-server. getRequestsByOwnerOfEvent success: size = {}", requests.size());

        return requestMapper.toDtoList(requests);
    }

}
