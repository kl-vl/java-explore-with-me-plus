package ru.practicum.stats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.stats.validate.ValidIpAddress;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    private Long id;

    @NotBlank
    @NotNull
    private String app;

    @NotBlank
    @NotNull
    private String uri;

    @NotNull
    @ValidIpAddress
    private String ip;

    @PastOrPresent
    private LocalDateTime timestamp;
}
