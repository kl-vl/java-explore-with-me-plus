package ru.practicum.mainservice.event;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.dto.EventFilterPublic;
import ru.practicum.mainservice.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecifications {

    public static Specification<Event> hasUsers(List<Long> userIds) {
        return (root, query, cb) -> {
            if (userIds == null || userIds.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("initiator").get("id").in(userIds);
        };
    }

    public static Specification<Event> hasStates(List<EventState> states) {
        return (root, query, cb) -> {
            if (states == null || states.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("state").in(states);
        };
    }

    public static Specification<Event> hasCategories(List<Long> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Event> inDateRange(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return cb.conjunction();
            }
            if (start != null && end != null) {
                return cb.between(root.get("eventDate"), start, end);
            }
            if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("eventDate"), start);
            }
            return cb.lessThanOrEqualTo(root.get("eventDate"), end);
        };
    }

    public static Specification<Event> hasText(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) {
                return cb.conjunction();
            }
            String searchText = "%" + text.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("annotation")), searchText),
                    cb.like(cb.lower(root.get("description")), searchText)
            );
        };
    }

    public static Specification<Event> isPaid(Boolean paid) {
        return (root, query, cb) -> {
            if (paid == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("paid"), paid);
        };
    }

    public static Specification<Event> isAvailable(Boolean onlyAvailable) {
        return (root, query, cb) -> {
            if (onlyAvailable == null || !onlyAvailable) {
                return cb.conjunction();
            }
            return cb.or(
                    cb.equal(root.get("participantLimit"), 0),
                    cb.lessThan(
                            root.get("confirmedRequests").get("count"),
                            root.get("participantLimit")
                    )
            );
        };
    }

    public static Specification<Event> hasStatePublished() {
        return (root, query, cb) -> cb.equal(root.get("state"), EventState.PUBLISHED);
    }

    public static Specification<Event> forPublicFilter(EventFilterPublic filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("state"), EventState.PUBLISHED));

            if (filter.getText() != null && !filter.getText().isBlank()) {
                String searchText = "%" + filter.getText().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("annotation")), searchText),
                        cb.like(cb.lower(root.get("description")), searchText)
                ));
            }

            if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(filter.getCategories()));
            }

            if (filter.getPaid() != null) {
                predicates.add(cb.equal(root.get("paid"), filter.getPaid()));
            }

            if (filter.getRangeStart() != null && filter.getRangeEnd() != null) {
                predicates.add(cb.between(root.get("eventDate"), filter.getRangeStart(), filter.getRangeEnd()));
            } else if (filter.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), filter.getRangeStart()));
            } else if (filter.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), filter.getRangeEnd()));
            }

            if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
                predicates.add(cb.or(
                        cb.equal(root.get("participantLimit"), 0),
                        cb.lessThan(
                                root.get("confirmedRequests"),
                                root.get("participantLimit")
                        )
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> forAdminFilter(EventFilterAdmin filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUsers() != null && !filter.getUsers().isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(filter.getUsers()));
            }

            if (filter.getStates() != null && !filter.getStates().isEmpty()) {
                predicates.add(root.get("state").in(filter.getStates()));
            }

            if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(filter.getCategories()));
            }

            if (filter.getRangeStart() != null && filter.getRangeEnd() != null) {
                predicates.add(cb.between(root.get("eventDate"), filter.getRangeStart(), filter.getRangeEnd()));
            } else if (filter.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), filter.getRangeStart()));
            } else if (filter.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), filter.getRangeEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}