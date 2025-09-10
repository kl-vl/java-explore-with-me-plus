package ru.practicum.statsserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.statsdto.EndpointHitDto;
import ru.practicum.statsdto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsServerRepository repository;
    private final EndpointHitMapper mapper;

    @Override
    public boolean addStat(EndpointHitDto endpointHitDto) {
        log.info("Stats-server. addStat input: uri = {}, app={} from ip {}",
                endpointHitDto.getUri(),
                endpointHitDto.getApp(),
                endpointHitDto.getIp());
        EndpointHitEntity savedEntity = repository.save(mapper.toEntity(endpointHitDto));
        log.info("Stats-server. addStat success: id = {}", savedEntity.getId());
        return true;
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Stats-server. getStat input: uris = {}, from {} to {}, unique = {}", uris.toString(), start, end, unique);
        List<ViewStatsDto> list = repository.getViewStats(uris, start, end, unique);
        log.info("Stats-server. getStat success: found {}", list.size());
        return list;
    }


}
