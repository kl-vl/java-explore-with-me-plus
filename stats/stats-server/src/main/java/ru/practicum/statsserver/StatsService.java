package ru.practicum.statsserver;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface StatsService {

    @Transactional
    boolean addStat(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
