package ru.practicum.mainservice.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    // getRequestsByOwnerOfEvent
    List<Request> findByEventIdAndEvent_InitiatorId(Long eventId, Long initiatorId);

    // createRequest - проверка существования запроса
    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    //  getCurrentUserRequests
    List<Request> findByRequesterId(Long requesterId);

    // cancelRequests
    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    // updateRequests - поиск запросов по ID для события
    @Query("SELECT r FROM Request r WHERE r.id IN :requestIds AND r.event.id = :eventId")
    List<Request> findByIdInAndEventId(@Param("requestIds") List<Long> requestIds,
                                       @Param("eventId") Long eventId);

    // лимит участников
    @Query("SELECT COUNT(r) FROM Request r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Long countConfirmedRequests(@Param("eventId") Long eventId);
}
