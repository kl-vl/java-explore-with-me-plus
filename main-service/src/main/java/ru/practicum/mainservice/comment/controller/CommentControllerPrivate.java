package ru.practicum.mainservice.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.CommentService;
import ru.practicum.mainservice.comment.dto.CommentDtoReq;
import ru.practicum.mainservice.exception.CommentNotFoundException;
import ru.practicum.mainservice.exception.EventNotFoundException;
import ru.practicum.mainservice.exception.UserNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Validated
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @Valid @RequestBody CommentDto commentDto) throws UserNotFoundException, EventNotFoundException {
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/{commentId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long eventId,
                              @Positive @PathVariable Long commentId) throws CommentNotFoundException {
        commentService.deleteComment(userId, eventId, commentId);
    }

    @PatchMapping("/{commentId}/events/{eventId}")
    public CommentDto updateComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @Positive @PathVariable Long commentId,
                                    @RequestBody @Validated CommentDtoReq commentDto) throws CommentNotFoundException {
        commentDto.setId(commentId);
        return commentService.updateComment(userId, eventId, commentDto);
    }

    @GetMapping
    public List<CommentDto> findUserComments(
            @PathVariable @Positive Long userId,
            @PageableDefault(page = 0, size = 10, sort = "created", direction = Sort.Direction.DESC) Pageable pageable) throws UserNotFoundException {
        return commentService.findUserComments(userId, pageable);
    }


}
