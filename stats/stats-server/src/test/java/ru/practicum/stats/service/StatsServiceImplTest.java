package ru.practicum.stats.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.exception.StartDateIsAfterEndDateException;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.model.EndpointHitEntity;
import ru.practicum.stats.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private StatsServerRepository repository;

    @Mock
    private EndpointHitMapper mapper;

    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void addStatShouldSaveEntityAndReturnTrue() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        boolean result = statsService.addStat(dto);

        assertTrue(result);
        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void addStatShouldLogBeforeAndAfterSaving() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);

        statsService.addStat(dto);

        verify(mapper, times(1)).toEntity(dto);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void getStatShouldReturnViewStatsFromRepository() throws StartDateIsAfterEndDateException {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        List<String> uris = List.of("/test1", "/test2");
        boolean unique = true;

        ViewStatsDto viewStats1 = new ViewStatsDto("app1", "/test1", 10L);
        ViewStatsDto viewStats2 = new ViewStatsDto("app2", "/test2", 5L);
        List<ViewStatsDto> expectedStats = List.of(viewStats1, viewStats2);

        when(repository.getViewStats(uris, start, end, unique)).thenReturn(expectedStats);

        List<ViewStatsDto> result = statsService.getStat(start, end, uris, unique);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedStats, result);
        verify(repository, times(1)).getViewStats(uris, start, end, unique);
    }

    @Test
    void getStatShouldHandleEmptyUrisList() throws StartDateIsAfterEndDateException {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        List<String> uris = List.of();
        boolean unique = false;

        ViewStatsDto viewStats = new ViewStatsDto("app", "/test", 15L);
        List<ViewStatsDto> expectedStats = List.of(viewStats);

        when(repository.getViewStats(uris, start, end, unique)).thenReturn(expectedStats);

        List<ViewStatsDto> result = statsService.getStat(start, end, uris, unique);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).getViewStats(uris, start, end, unique);
    }

    @Test
    void getStatShouldHandleEmptyResult() throws StartDateIsAfterEndDateException {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        List<String> uris = List.of("/nonexistent");
        boolean unique = true;

        when(repository.getViewStats(uris, start, end, unique)).thenReturn(List.of());

        List<ViewStatsDto> result = statsService.getStat(start, end, uris, unique);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).getViewStats(uris, start, end, unique);
    }

    @Test
    void getStatShouldHandleUniqueFalse() throws StartDateIsAfterEndDateException {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 0, 0);
        List<String> uris = List.of("/test");
        boolean unique = false;

        ViewStatsDto viewStats = new ViewStatsDto("app", "/test", 25L);
        List<ViewStatsDto> expectedStats = List.of(viewStats);

        when(repository.getViewStats(uris, start, end, unique)).thenReturn(expectedStats);

        List<ViewStatsDto> result = statsService.getStat(start, end, uris, unique);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).getViewStats(uris, start, end, unique);
    }
}