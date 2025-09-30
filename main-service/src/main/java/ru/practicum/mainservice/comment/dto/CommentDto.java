package ru.practicum.mainservice.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.mainservice.comment.CommentStatus;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class CommentDto {
    private Long id;
    private String text;
    private CommentStatus status;
    private Long authorId;
    private LocalDateTime created;
    private Long eventId;

}
