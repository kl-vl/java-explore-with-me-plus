package ru.practicum.statsserver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    @Override
    public boolean addStat(EndpointHitDto endpointHitDto) {
        // TODO реализация
        return false;
    }

    @Override
    public List<ViewStatsDto> getStat(String start, String end, List<String> uris, Boolean unique) {
        // TODO реализация
        return Collections.emptyList();
    }


}
