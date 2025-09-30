package ru.practicum.mainservice.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentDtoReq;
import ru.practicum.mainservice.comment.dto.CommentDtoShort;
import ru.practicum.mainservice.comment.dto.CommentDtoStatus;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.event.EventRepository;
import ru.practicum.mainservice.exception.CommentNotFoundException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.UserNotFoundException;
import ru.practicum.mainservice.user.User;
import ru.practicum.mainservice.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto findComment(Long commentId) throws CommentNotFoundException {
        log.info("Main-service. findComment id = {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден с ID %d".formatted(commentId)));
        log.info("Main-service. findComment succes id = {}", comment.getId());
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentDto findComment(Long eventId, Long commentId) throws CommentNotFoundException {
        log.info("Main-service. findComment eventId = {}, commentId {}", eventId, commentId);
        Comment comment = commentRepository.findByIdAndEventId(commentId, eventId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "Комментарий не найден с ID %d для события с ID %d ".formatted(commentId, eventId)));
        log.info("Main-service. findComment success eventId = {}, commentId = {}", comment.getEvent().getId(), comment.getId());
        return commentMapper.toDto(comment);
    }

    @Override
    public List<CommentDtoShort> findEventComments(Long eventId, Pageable pageable) throws EventNotFoundException {
        log.info("Main-service. findEventComments eventId = {}, c пагинацией = {}", eventId, pageable);
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Событие не найдено с ID %d".formatted(eventId));
        }
        Page<Comment> comments = commentRepository.findByEventIdAndStatus(eventId, CommentStatus.PUBLISHED, pageable);

        log.info("Main-service. findEventComments success = {}", comments.getSize());

        return comments.stream()
                .map(commentMapper::toDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findUserComments(Long userId, Pageable pageable) throws UserNotFoundException {
        log.info("Main-service. findUserComments userId = {}", userId);
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с ID %d".formatted(userId)));

        Page<Comment> comments = commentRepository.findByAuthor_Id(userId, pageable);

        log.info("Main-service. findUserComments success = {}", comments.getSize());

        return comments.stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) throws UserNotFoundException, EventNotFoundException {
        log.info("Main-service. createComment userId = {}, eventId = {}", userId, eventId);
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с ID %d".formatted(userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено с ID %d".formatted(eventId)));
        Comment comment = commentMapper.toEntity(commentDto);
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);

        Comment savedComment = commentRepository.save(comment);
        log.info("Main-service. createComment success id = {}", savedComment.getId());
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(CommentDtoStatus commentDto) throws CommentNotFoundException {
        log.info("Main-service. updateComment commentId = {} ", commentDto.getId());
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new CommentNotFoundException(("Комментарий не найден с ID %d".formatted(commentDto.getId()))));
        if (commentDto.getStatus() != null) {
            comment.setStatus(commentDto.getStatus());
        }
        Comment updatedComment = commentRepository.save(comment);
        log.info("Main-service. updateComment success id = {} ", updatedComment.getId());
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long eventId, CommentDtoReq commentDto) throws CommentNotFoundException {
        log.info("Main-service. updateComment userId = {}, eventId = {}, commentId = {} ", userId, eventId, commentDto.getId());
        Comment comment = commentRepository.findByIdAndEventIdAndAuthorId(commentDto.getId(), eventId, userId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "Комментарий c ID %d не найден для события с ID %d или у вас нет прав для редактирования"
                                .formatted(commentDto.getId(), eventId)));

        // При редактировании комментария пользователем возвращаем на модерацию
        Optional.ofNullable(commentDto.getText()).ifPresent(text -> {
            comment.setText(text);
            comment.setStatus(CommentStatus.PENDING);
        });

        Comment updatedComment = commentRepository.save(comment);
        log.info("Main-service. updateComment success id = {} ", updatedComment.getId());
        return commentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) throws CommentNotFoundException {
        log.info("Main-service. deleteComment commentId = {} ", commentId);
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFoundException("Комментарий не найден с ID %d".formatted(commentId));
        }
        commentRepository.deleteById(commentId);
        log.info("Main-service. deleteComment success commentId = {} ", commentId);
    }

    @Override
    @Transactional
    public void deleteComment(Long eventId, Long commentId) throws CommentNotFoundException {
        log.info("Main-service. deleteComment eventId = {}, commentId = {} ", eventId, commentId);
        if (!commentRepository.existsByIdAndEventId(commentId, eventId)) {
            throw new CommentNotFoundException("Комментарий c ID %d не найден для события c ID %d".formatted(commentId, eventId));
        }
        commentRepository.deleteByIdAndEventId(commentId, eventId);
        log.info("Main-service. deleteComment success commentId = {} ", commentId);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long eventId, Long commentId) throws CommentNotFoundException {
        log.info("Main-service. deleteComment userId = {}, eventId = {}, commentId = {} ", userId, eventId, commentId);

        if (!commentRepository.existsByIdAndEventIdAndAuthorId(commentId, eventId, userId)) {
            throw new CommentNotFoundException(
                    "Комментарий c ID %d не найден для события с ID %d или у вас нет прав для для удаления"
                            .formatted(commentId, eventId));
        }
        commentRepository.deleteByIdAndEventIdAndAuthorId(commentId, eventId, userId);
        log.info("Main-service. deleteComment success id = {}", commentId);
    }

}
