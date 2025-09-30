package ru.practicum.mainservice.subscription.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.subscription.model.Subscription;
import ru.practicum.mainservice.subscription.model.SubscriptionDtoProjection;

import java.util.List;

public interface SubscriptionService {

    Subscription create(Long subscription, Long user) throws UserNotFoundException;

    void delete(Long userId, Long subscriptionId);

    List<SubscriptionDtoProjection> getAllSubscriptions(Long id);

    Page<Event> getAllEvents(Long userId, Pageable pageable);
}