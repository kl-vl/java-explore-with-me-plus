package ru.practicum.mainservice.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.practicum.mainservice.category.Category;
import ru.practicum.mainservice.category.CategoryRepository;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventDtoFull;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.dto.EventFilterBase;
import ru.practicum.mainservice.event.dto.EventFilterPublic;
import ru.practicum.mainservice.event.enums.EventState;
import ru.practicum.mainservice.event.enums.EventStateAction;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.EventAlreadyPublishedException;
import ru.practicum.mainservice.exception.EventCanceledCantPublishException;
import ru.practicum.mainservice.exception.EventDateException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.EventValidationException;
import ru.practicum.mainservice.exception.PaginatorValidationException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.location.Location;
import ru.practicum.mainservice.location.LocationRepository;
import ru.practicum.mainservice.user.User;
import ru.practicum.mainservice.user.UserRepository;
import ru.practicum.stats.ClientRestStat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ClientRestStat clientRestStat;
    private final EventMapper eventMapper;
    private final Validator validator;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    private void validateFilter(EventFilterBase filter) throws PaginatorValidationException, EventDateException {

        validateDateRange(filter.getRangeStart(), filter.getRangeEnd());

        // TODO Для поиска такая проверка не нужна, проверить на полных тестах
        //
        //        if (filter instanceof EventFilterPublic) {
        //            if (filter.getRangeStart() != null && filter.getRangeStart().isBefore(LocalDateTime.now())) {
        //                throw new EventDateException("Start date cannot be in the past");
        //            }
        //        }

        Errors errors = new BeanPropertyBindingResult(filter, "filter");
        ValidationUtils.invokeValidator(validator, filter, errors);

        if (errors.hasErrors()) {
            String errorMessage = errors.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            throw new PaginatorValidationException("Filter validation failed: " + errorMessage);
        }
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) throws EventDateException {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventDateException("Start date must be before end date");
        }
    }

    private void setDefaultValues(EventDto eventDto) {
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }
    }

    private List<EventDtoFull> getDtoFullList(Page<Event> pageEvents) {
        return pageEvents.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private void addHit(HttpServletRequest request) {
        // TODO пока отключен вызов, доделываем локально всё в main-service
        return;
/*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        clientRestStat.addStat(endpointHitDto);
*/
    }

    // Admin
    @Override
    public List<EventDtoFull> findEventsByUsers(EventFilterAdmin eventFilter) throws PaginatorValidationException, EventDateException {
        //validatePaginator(eventFilter);
        validateFilter(eventFilter);

        log.info("Main-server. findEventsByUsers input: filter = {}", eventFilter.toString());

        Specification<Event> specification = EventSpecifications.forAdminFilter(eventFilter);

        Pageable pageable = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(), eventFilter.getSize());

        Page<Event> pageEvents = eventRepository.findAll(specification, pageable);
        final List<EventDtoFull> events = getDtoFullList(pageEvents);

        log.info("Main-server. findEventsByUsers success: size = {}", events.size());

        return events;
    }

    @Override
    @Transactional
    public EventDtoFull updateEventById(EventDto eventDto) throws EventNotFoundException, EventValidationException, EventDateException, EventAlreadyPublishedException, EventCanceledCantPublishException {
        log.info("Main-server. updateEventById input: id = {}", eventDto.getId());
        Event event = eventRepository.findById(eventDto.getId())
                .orElseThrow(() -> new EventNotFoundException("Event with id=%d was not found".formatted(eventDto.getId())));

        /*
         * Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:
         * дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
         * событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
         * событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
         */

        if (eventDto.getEventDate() != null && !eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
            throw new EventDateException("Event date should be in 1+ hours after now");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventAlreadyPublishedException("Event is already published");
        }

        if (event.getState().equals(EventState.CANCELED)) {
            throw new EventCanceledCantPublishException("Canceled event cant be published");
        }

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (eventDto.getStateAction().equals(EventStateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        //eventMapper.onUpdateEventFromDto(eventDto, event);
        eventMapper.updateEventFromDto(eventDto, event);
        Event updatedEvent = eventRepository.save(event);

        log.info("Main-server. updateEventById success: id = {}", updatedEvent.getId());

        return eventMapper.toEventFullDto(updatedEvent);
    }

    // Private
    @Override
    @Transactional
    public EventDtoFull createEvent(EventDto eventDto) throws
            CategoryNotFoundException, UserNotFoundException, EventDateException {
        log.info("Main-server. createEvent input: id = {}", eventDto.getDescription());

        setDefaultValues(eventDto);

        //log.info("Main-server. createEvent input: eventMapper.toEvent(eventDto) = {}", eventMapper.toEvent(eventDto).toString());

        if (eventDto.getEventDate() != null && !eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
            throw new EventDateException("Event date should be in 1+ hours after now");
        }

        Event event = eventMapper.toEvent(eventDto);
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id=%d was not found", eventDto.getCategory())));
            event.setCategory(category);
        }

        if (eventDto.getInitiator() != null) {
            User user = userRepository.findById(eventDto.getInitiator())
                    .orElseThrow(() -> new UserNotFoundException(String.format("User with id=%d was not found", eventDto.getInitiator())));
            event.setInitiator(user);
        }

        if (eventDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(
                    eventDto.getLocation().getLat(),
                    eventDto.getLocation().getLon()
            ).orElseGet(() -> {
                Location newLocation = new Location();
                newLocation.setLat(eventDto.getLocation().getLat());
                newLocation.setLon(eventDto.getLocation().getLon());
                return locationRepository.save(newLocation);
            });
            event.setLocation(location);
        }

        // TODO
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setConfirmedRequests(0L);
        event.setViews(0L);

        Event createdEvent = eventRepository.save(event);

        log.info("Main-server. createEvent success: id = {}", createdEvent.getId());

        return eventMapper.toEventFullDto(createdEvent);
    }

    @Override
    public EventDtoFull findEventByUserId(Long userId, Long eventId) throws EventNotFoundException {
        log.info("Main-server. findEventByUserId input: userId = {}, eventId = {}", userId, eventId);

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        log.info("Main-server. findEventByUserId success: id = {}", event.getId());

        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventDtoFull> findEventsByUserid(Long userId, int from, int size) {
        log.info("Main-server. findEventsByUserid input: userId = {}, from = {}, size = {}", userId, from, size);

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Event> pageEvents = eventRepository.findAllByInitiatorId(userId, pageable);
        final List<EventDtoFull> events = getDtoFullList(pageEvents);

        log.info("Main-server. findEventsByUserid success: id = {}", events.size());

        return events;
    }

    @Override
    @Transactional
    public EventDtoFull updateEventByUserId(EventDto eventDto) throws EventNotFoundException, EventDateException, EventCanceledCantPublishException {
        log.info("Main-server. updateEventByUserId input: eventId = {}, userId = {}", eventDto.getId(), eventDto.getInitiator());

        // изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
        //дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
        if (eventDto.getEventDate() != null && !eventDto.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException("Event date should be in 2+ hours after now");
        }

        // TODO в транзакции полный запрос со всеми связями не нужен
        //        Event existingEvent = eventRepository.findByIdWithCategoryAndInitiator(eventDto.getId(), eventDto.getInitiator())
        //                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventDto.getId() + " not found"));
        Event existingEvent = eventRepository.findByIdAndInitiatorId(eventDto.getId(), eventDto.getInitiator())
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventDto.getId() + " not found"));

        if (!(existingEvent.getState() == EventState.PENDING || existingEvent.getState() == EventState.CANCELED)) {
            throw new EventCanceledCantPublishException("Event can be edited only Pending or Canceled");
        }

        //eventMapper.updateSimpleFieldsFromDto(eventDto, existingEvent);
        eventMapper.updateEventFromDto(eventDto, existingEvent);

        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW)) {
                existingEvent.setState(EventState.PENDING);
            } else {
                existingEvent.setState(EventState.CANCELED);
            }
        }
        Event updatedEvent = eventRepository.save(existingEvent);

        log.info("Main-server. updateEventByUserId success: eventId = {}", updatedEvent.getId());

        return eventMapper.toEventFullDto(updatedEvent);
    }

    //Public
    @Override
    public List<EventDtoFull> findEvents(EventFilterPublic eventFilter, HttpServletRequest request) throws
            PaginatorValidationException, EventDateException {
        validateFilter(eventFilter);

        log.info("Main-server. findEventsByUsers input: filter = {}", eventFilter.toString());

        // вызов stat-client
        addHit(request);

        Specification<Event> specification = EventSpecifications.forPublicFilter(eventFilter);
        Pageable pageable = PageRequest.of(eventFilter.getFrom() / eventFilter.getSize(), eventFilter.getSize());
        Page<Event> pageEvents = eventRepository.findAll(specification, pageable);
        final List<EventDtoFull> events = getDtoFullList(pageEvents);

        log.info("Main-server. findEventsByUsers success: size = {}", events.size());

        return events;
    }

    @Override
    public EventDtoFull findEventById(Long eventId, HttpServletRequest request) throws EventNotFoundException {
        log.info("Main-server. findEventById input: eventId = {}", eventId);

        // вызов stat-client
        addHit(request);

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        log.info("Main-server. findEventById success: eventId = {}", event.getId());

        return eventMapper.toEventFullDto(event);
    }

}