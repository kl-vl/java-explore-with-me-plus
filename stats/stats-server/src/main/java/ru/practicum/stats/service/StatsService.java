package ru.practicum.stats.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface StatsService {

    @Transactional
    boolean addStat(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
