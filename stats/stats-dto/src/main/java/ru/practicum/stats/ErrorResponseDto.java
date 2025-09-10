package ru.practicum.stats;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@RequiredArgsConstructor
public class ErrorResponseDto {
    private final String message;
    private final String error;
/*
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;
*/
    private final Map<String, String> details;

}
