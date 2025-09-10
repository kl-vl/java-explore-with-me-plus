package ru.practicum.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRestStat {
    Boolean addStat(EndpointHitDto dto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
