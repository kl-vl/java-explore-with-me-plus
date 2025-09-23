package ru.practicum.stats.service;

import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.exception.StartDateIsAfterEndDateException;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    boolean addStat(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws StartDateIsAfterEndDateException;

}
