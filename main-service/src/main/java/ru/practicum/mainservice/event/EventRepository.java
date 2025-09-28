package ru.practicum.mainservice.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.event.enums.EventState;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Page<Event> findAllByInitiatorIdIn(List<Long> userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, EventState eventState);

    @Query("SELECT e FROM Event e WHERE e.id = :id AND e.initiator.id = :initiatorId")
    Optional<Event> findByIdWithCategoryAndInitiator(@Param("id") Long id,
                                                     @Param("initiatorId") Long initiatorId);

    boolean existsByCategoryId(Long categoryId);
}
