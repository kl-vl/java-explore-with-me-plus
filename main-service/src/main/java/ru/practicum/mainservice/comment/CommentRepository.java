package ru.practicum.mainservice.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndEventId(Long commentId, Long eventId);

    Optional<Comment> findByIdAndEventIdAndAuthorId(Long commentId, Long eventId, Long userId);

    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    List<Comment> findByEventId(Long eventId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.event.id = :eventId")
    void deleteByIdAndEventId(@Param("commentId") Long commentId, @Param("eventId") Long eventId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :commentId AND c.event.id = :eventId AND c.author.id = :userId")
    void deleteByIdAndEventIdAndAuthorId(@Param("commentId") Long commentId,
                                         @Param("eventId") Long eventId,
                                         @Param("userId") Long userId);

    boolean existsByIdAndEventIdAndAuthorId(Long commentId, Long eventId, Long userId);

    Page<Comment> findByEventIdAndStatus(Long eventId, CommentStatus status, Pageable pageable);

    Page<Comment> findByAuthor_Id(Long userId, Pageable pageable);

    long countByEventId(Long eventId);

    Optional<Comment> findFirstByEventId(Long eventId);

    boolean existsByIdAndEventId(Long commentId, Long eventId);
}
