package ru.practicum.mainservice.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.request.dto.ConfirmedRequestsCount;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    // getRequestsByOwnerOfEvent
    List<Request> findByEventIdAndEvent_InitiatorId(Long eventId, Long initiatorId);

    // createRequest
    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    //  getCurrentUserRequests
    List<Request> findByRequesterId(Long requesterId);

    // cancelRequests
    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    // updateRequests
    @Query("SELECT r FROM Request r WHERE r.id IN :requestIds AND r.event.id = :eventId")
    List<Request> findByIdInAndEventId(@Param("requestIds") List<Long> requestIds,
                                       @Param("eventId") Long eventId);

    // лимит участников
    @Query("SELECT COUNT(r) FROM Request r WHERE r.event.id = :eventId AND r.status = 'CONFIRMED'")
    Long countConfirmedRequests(@Param("eventId") Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Request r SET r.status = :status " +
            "WHERE r.id IN :requestIds AND r.event.id = :eventId")
    int updateRequestsStatus(@Param("requestIds") List<Long> requestIds,
                             @Param("eventId") Long eventId,
                             @Param("status") RequestStatus status);

    @Query("SELECT r FROM Request r WHERE r.id IN :requestIds AND r.event.id = :eventId AND r.status = :status")
    List<Request> findRequestsByStatus(@Param("requestIds") List<Long> requestIds,
                                       @Param("eventId") Long eventId,
                                       @Param("status") RequestStatus status);

    @Query("SELECT r.event.id as eventId, COUNT(r) as count " +
            "FROM Request r " +
            "WHERE r.event.id IN :eventIds " +
            "AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event.id")
    List<ConfirmedRequestsCount> findConfirmedRequestsCountByEventIds(@Param("eventIds") List<Long> eventIds);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);
}
