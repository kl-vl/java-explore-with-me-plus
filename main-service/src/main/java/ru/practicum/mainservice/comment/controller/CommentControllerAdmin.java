package ru.practicum.mainservice.comment.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.comment.CommentService;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentDtoStatus;
import ru.practicum.mainservice.exception.CommentNotFoundException;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@Validated
public class CommentControllerAdmin {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto findComment(@PathVariable @Positive Long commentId) throws CommentNotFoundException {
        return commentService.findComment(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Positive @PathVariable Long commentId) throws CommentNotFoundException {
        commentService.deleteComment(commentId);
    }

    // публикацция или сокрытие комментария админом
    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@Positive @PathVariable Long commentId, @RequestBody  @Validated CommentDtoStatus commentDto) throws CommentNotFoundException {
        commentDto.setId(commentId);
        return commentService.updateComment(commentDto);
    }

}
