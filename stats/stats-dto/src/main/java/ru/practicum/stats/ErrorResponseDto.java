package ru.practicum.stats;

import lombok.*;

import java.util.Map;

@Builder
@RequiredArgsConstructor
@Getter
public class ErrorResponseDto {
    private final String message;
    private final String error;
    private final Map<String, String> details;

}
