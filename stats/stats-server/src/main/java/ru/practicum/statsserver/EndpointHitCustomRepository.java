package ru.practicum.statsserver;

import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitCustomRepository {
    List<ViewStatsDto> getViewStats(String[] uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
