package ru.practicum.mainservice.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.event.EventRepository;
import ru.practicum.mainservice.subscription.model.Subscription;
import ru.practicum.mainservice.subscription.model.SubscriptionDtoProjection;
import ru.practicum.mainservice.subscription.storage.SubscriptionRepository;
import ru.practicum.mainservice.user.User;
import ru.practicum.mainservice.user.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<SubscriptionDtoProjection> getAllSubscriptions(Long id) {
        return subscriptionRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public Subscription create(Long userId, Long subscriptionId) {

        Subscription subscriptionSave = new Subscription();

        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(subscriptionSave::setUser);

        Optional<User> subscription = userRepository.findById(subscriptionId);
        subscription.ifPresent(subscriptionSave::setSubscription);

        return subscriptionRepository.save(subscriptionSave);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long subscriptionId) {
        subscriptionRepository.deleteByUserIdAndSubscriptionId(userId, subscriptionId);
    }

    @Override
    public Page<Event> getAllEvents(Long userId, Pageable pageable) {

        List<SubscriptionDtoProjection> subscription = getAllSubscriptions(userId);

        if (subscription.isEmpty()) {
            return Page.empty();
        }

        List<Long> subscribedUserIds = subscription.stream()
                .map(SubscriptionDtoProjection::getSubscription)
                .map(User::getId)
                .toList();

        return eventRepository.findAllByInitiatorIdIn(subscribedUserIds, pageable);
    }
}