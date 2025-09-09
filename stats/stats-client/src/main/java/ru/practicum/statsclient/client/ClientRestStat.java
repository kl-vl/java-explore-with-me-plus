package ru.practicum.statsclient.client;

import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRestStat {
    EndpointHitDto addStat(EndpointHitDto dto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
