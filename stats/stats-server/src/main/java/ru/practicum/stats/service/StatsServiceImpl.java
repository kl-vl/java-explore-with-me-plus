package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.exception.StartDateIsAfterEndDateException;
import ru.practicum.stats.model.EndpointHitEntity;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsServerRepository statsServerRepository;
    private final EndpointHitMapper endpointHitMapper;

    @Override
    @Transactional
    public boolean addStat(EndpointHitDto endpointHitDto) {
        log.info("Stats-server. addStat input: uri = {}, app={} from ip {}",
                endpointHitDto.getUri(),
                endpointHitDto.getApp(),
                endpointHitDto.getIp());

        EndpointHitEntity savedEntity = statsServerRepository.save(endpointHitMapper.toEntity(endpointHitDto));

        log.info("Stats-server. addStat success: id = {}", savedEntity.getId());

        return true;
    }

    @Override
    public List<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws StartDateIsAfterEndDateException {
        log.info("Stats-server. getStat input: uris = {}, from {} to {}, unique = {}", uris.toString(), start, end, unique);

        if (start.isAfter(end)) {
            throw new StartDateIsAfterEndDateException("Start date " + start + " cannot be after end date " + end);
        }

        List<ViewStatsDto> list = statsServerRepository.getViewStats(uris, start, end, unique);

        log.info("Stats-server. getStat success: found {}", list.size());

        return list;
    }

}
