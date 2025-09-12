package ru.practicum.stats.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.model.EndpointHitEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class EndpointHitCustomRepositoryImplIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EndpointHitCustomRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        entityManager.createQuery("DELETE FROM EndpointHitEntity").executeUpdate();

        createTestData();
    }

    private void createTestData() {
        EndpointHitEntity hit1 = EndpointHitEntity.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.of(2023, 1, 1, 12, 0))
                .build();

        EndpointHitEntity hit2 = EndpointHitEntity.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.1.2")
                .timestamp(LocalDateTime.of(2023, 1, 1, 13, 0))
                .build();

        EndpointHitEntity hit3 = EndpointHitEntity.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.1.1") // Тот же IP, что и hit1
                .timestamp(LocalDateTime.of(2023, 1, 1, 14, 0))
                .build();

        EndpointHitEntity hit4 = EndpointHitEntity.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("192.168.1.3")
                .timestamp(LocalDateTime.of(2023, 1, 1, 15, 0))
                .build();

        EndpointHitEntity hit5 = EndpointHitEntity.builder()
                .app("another-service")
                .uri("/events/1")
                .ip("192.168.1.4")
                .timestamp(LocalDateTime.of(2023, 1, 1, 16, 0))
                .build();

        entityManager.persist(hit1);
        entityManager.persist(hit2);
        entityManager.persist(hit3);
        entityManager.persist(hit4);
        entityManager.persist(hit5);
        entityManager.flush();
    }

    @Test
    void getViewStatsWithUniqueTrueShouldCountDistinctIps() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1", "/events/2");

        List<ViewStatsDto> result = repository.getViewStats(uris, start, end, true);

        assertNotNull(result);
        assertEquals(3, result.size());

        ViewStatsDto event1 = findStatsByAppAndUri(result, "ewm-main-service", "/events/1");
        assertNotNull(event1);
        assertEquals(2L, event1.getHits());

        ViewStatsDto event2 = findStatsByAppAndUri(result, "ewm-main-service", "/events/2");
        assertNotNull(event2);
        assertEquals(1L, event2.getHits());

        ViewStatsDto event3 = findStatsByAppAndUri(result, "another-service", "/events/1");
        assertNotNull(event3);
        assertEquals(1L, event3.getHits());
    }

    @Test
    void getViewStatsWithUniqueFalseShouldCountAllHits() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1");

        List<ViewStatsDto> result = repository.getViewStats(uris, start, end, false);

        assertNotNull(result);
        assertEquals(2, result.size());

        ViewStatsDto service1 = findStatsByAppAndUri(result, "ewm-main-service", "/events/1");
        assertNotNull(service1);
        assertEquals(3L, service1.getHits());

        ViewStatsDto service2 = findStatsByAppAndUri(result, "another-service", "/events/1");
        assertNotNull(service2);
        assertEquals(1L, service2.getHits());
    }

    @Test
    void getViewStatsWithTimeRangeShouldFilterByTime() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 15, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1", "/events/2");

        List<ViewStatsDto> result = repository.getViewStats(uris, start, end, false);

        assertNotNull(result);
        assertEquals(2, result.size());

        ViewStatsDto event2 = findStatsByAppAndUri(result, "ewm-main-service", "/events/2");
        assertNotNull(event2);
        assertEquals(1L, event2.getHits());

        ViewStatsDto anotherService = findStatsByAppAndUri(result, "another-service", "/events/1");
        assertNotNull(anotherService);
        assertEquals(1L, anotherService.getHits());
    }

    @Test
    void getViewStatsWithEmptyUrisShouldReturnAllUris() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);

        List<ViewStatsDto> result = repository.getViewStats(List.of(), start, end, false);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void getViewStatsWithNullUrisShouldReturnAllUris() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);

        List<ViewStatsDto> result = repository.getViewStats(null, start, end, false);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void getViewStatsShouldOrderByHitsDescending() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 0, 0);
        List<String> uris = List.of("/events/1", "/events/2");

        List<ViewStatsDto> result = repository.getViewStats(uris, start, end, false);

        assertNotNull(result);
        assertEquals(3, result.size());

        assertTrue(result.get(0).getHits() >= result.get(1).getHits());
        assertTrue(result.get(1).getHits() >= result.get(2).getHits());
    }

    private ViewStatsDto findStatsByAppAndUri(List<ViewStatsDto> stats, String app, String uri) {
        return stats.stream()
                .filter(dto -> dto.getApp().equals(app) && dto.getUri().equals(uri))
                .findFirst()
                .orElse(null);
    }
}