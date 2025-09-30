package ru.practicum.mainservice.subscription.controller;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.subscription.model.Subscription;
import ru.practicum.mainservice.subscription.model.SubscriptionDtoProjection;
import ru.practicum.mainservice.subscription.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{subscription}")
    public Subscription create(
            @PathVariable(name = "id") @PositiveOrZero Long userId,
            @PathVariable(name = "subscription") @PositiveOrZero Long subscriptionId
    ) throws UserNotFoundException {
        return subscriptionService.create(userId, subscriptionId);
    }

    @DeleteMapping("/{subscription}")
    public void delete(
            @PathVariable(name = "id") @PositiveOrZero Long userId,
            @PathVariable(name = "subscription") @PositiveOrZero Long subscriptionId) {
        subscriptionService.delete(userId, subscriptionId);
    }

    @GetMapping
    public List<SubscriptionDtoProjection> getAllSubscriptions(
            @PathVariable(name = "id") @PositiveOrZero Long id
    ) {
        return subscriptionService.getAllSubscriptions(id);
    }

    @GetMapping("/events")
    public Page<Event> getAllEvents(
            @PathVariable(name = "id") @PositiveOrZero Long userId,
            Pageable pageable
    ) {
        return subscriptionService.getAllEvents(userId, pageable);
    }
}