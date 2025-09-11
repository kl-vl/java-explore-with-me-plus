package ru.practicum.stats.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EndpointHitEntityTest {

    @Test
    void shouldCreateEntityWithBuilder() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test/endpoint")
                .ip("192.168.1.1")
                .timestamp(timestamp)
                .build();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("test-app", entity.getApp());
        assertEquals("/test/endpoint", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(timestamp, entity.getTimestamp());
    }

    @Test
    void shouldCreateEntityWithNoArgsConstructor() {
        EndpointHitEntity entity = new EndpointHitEntity();

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getApp());
        assertNull(entity.getUri());
        assertNull(entity.getIp());
        assertNull(entity.getTimestamp());
    }

    @Test
    void shouldCreateEntityWithAllArgsConstructor() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 10, 30, 0);

        EndpointHitEntity entity = new EndpointHitEntity(
                1L,
                "test-app",
                "/test/endpoint",
                "192.168.1.1",
                timestamp
        );

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("test-app", entity.getApp());
        assertEquals("/test/endpoint", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(timestamp, entity.getTimestamp());
    }

    @Test
    void shouldSetAndGetProperties() {
        EndpointHitEntity entity = new EndpointHitEntity();
        LocalDateTime timestamp = LocalDateTime.now();

        entity.setId(1L);
        entity.setApp("test-app");
        entity.setUri("/test/endpoint");
        entity.setIp("192.168.1.1");
        entity.setTimestamp(timestamp);

        assertEquals(1L, entity.getId());
        assertEquals("test-app", entity.getApp());
        assertEquals("/test/endpoint", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(timestamp, entity.getTimestamp());
    }

    @Test
    void shouldBeEqualWhenSameInstance() {
        LocalDateTime timestamp = LocalDateTime.now();
        EndpointHitEntity entity1 = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(timestamp)
                .build();

        EndpointHitEntity entity2 = entity1;
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldBeEqualWhenSameValues() {
        LocalDateTime timestamp = LocalDateTime.now();
        EndpointHitEntity entity1 = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(timestamp)
                .build();

        EndpointHitEntity entity2 = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(timestamp)
                .build();

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        LocalDateTime timestamp = LocalDateTime.now();
        EndpointHitEntity entity1 = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(timestamp)
                .build();

        EndpointHitEntity entity2 = EndpointHitEntity.builder()
                .id(2L)
                .app("different-app")
                .uri("/different")
                .ip("192.168.1.2")
                .timestamp(timestamp.plusHours(1))
                .build();

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenNull() {
        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.now())
                .build();

        assertNotEquals(null, entity);
    }

    @Test
    void shouldHaveCorrectToString() {
        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0))
                .build();

        String toString = entity.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("app=test-app"));
        assertTrue(toString.contains("uri=/test"));
        assertTrue(toString.contains("ip=192.168.1.1"));
        assertTrue(toString.contains("timestamp=2024-01-15T10:30"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldHandleBlankStrings(String value) {
        EndpointHitEntity entity = new EndpointHitEntity();

        entity.setApp(value);
        entity.setUri(value);
        entity.setIp(value);

        assertEquals(value, entity.getApp());
        assertEquals(value, entity.getUri());
        assertEquals(value, entity.getIp());
    }

    @Test
    void shouldHandleNullTimestamp() {
        EndpointHitEntity entity = new EndpointHitEntity();

        entity.setTimestamp(null);

        assertNull(entity.getTimestamp());
    }
}