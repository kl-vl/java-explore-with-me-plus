package ru.practicum.mainservice.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class CommentDtoReq {
    @Null
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 4000)
    private String text;

}
