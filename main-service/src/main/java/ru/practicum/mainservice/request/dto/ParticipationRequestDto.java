package ru.practicum.mainservice.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.request.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    @NotNull
    private Long event;

    @NotNull
    private Long requester;

    private RequestStatus status;

    private LocalDateTime created;

}
