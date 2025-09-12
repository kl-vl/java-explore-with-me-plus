package ru.practicum.stats.repository;

import ru.practicum.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitCustomRepository {
    List<ViewStatsDto> getViewStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
