package ru.practicum.mainservice.event;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.dto.EventFilterPublic;
import ru.practicum.mainservice.event.enums.EventState;

import java.util.ArrayList;
import java.util.List;

public class EventSpecifications {

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