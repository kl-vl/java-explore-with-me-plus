package ru.practicum.stats.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.model.EndpointHitEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EndpointHitMapperTest {

    private final EndpointHitMapper mapper = Mappers.getMapper(EndpointHitMapper.class);

    @Test
    void toEntityShouldMapDtoToEntityIgnoringId() {

        EndpointHitDto dto = EndpointHitDto.builder()
                .app("test-app")
                .uri("/test/endpoint")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0))
                .build();

        EndpointHitEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("test-app", entity.getApp());
        assertEquals("/test/endpoint", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), entity.getTimestamp());
    }

    @Test
    void toEntityShouldHandleNullDto() {
        EndpointHitEntity entity = mapper.toEntity(null);

        assertNull(entity);
    }

    @Test
    void toDtoShouldMapEntityToDto() {
        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app("test-app")
                .uri("/test/endpoint")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0))
                .build();

        EndpointHitDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("test-app", dto.getApp());
        assertEquals("/test/endpoint", dto.getUri());
        assertEquals("192.168.1.1", dto.getIp());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), dto.getTimestamp());
    }

    @Test
    void toDtoShouldHandleNullEntity() {
        EndpointHitDto dto = mapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toEntityShouldMapDtoWithNullValues() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .id(1L)
                .app(null)
                .uri(null)
                .ip(null)
                .timestamp(null)
                .build();

        EndpointHitEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getApp());
        assertNull(entity.getUri());
        assertNull(entity.getIp());
        assertNull(entity.getTimestamp());
    }

    @Test
    void toDtoShouldMapEntityWithNullValues() {
        EndpointHitEntity entity = EndpointHitEntity.builder()
                .id(1L)
                .app(null)
                .uri(null)
                .ip(null)
                .timestamp(null)
                .build();

        EndpointHitDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNull(dto.getApp());
        assertNull(dto.getUri());
        assertNull(dto.getIp());
        assertNull(dto.getTimestamp());
    }

    @Test
    void bidirectionalMappingShouldWorkCorrectly() {
        EndpointHitDto originalDto = EndpointHitDto.builder()
                .id(999L)
                .app("test-app")
                .uri("/test/endpoint")
                .ip("192.168.1.1")
                .timestamp(LocalDateTime.of(2024, 1, 15, 10, 30, 0))
                .build();

        EndpointHitEntity entity = mapper.toEntity(originalDto);
        EndpointHitDto resultDto = mapper.toDto(entity);

        assertNotNull(resultDto);
        assertNull(resultDto.getId());
        assertEquals("test-app", resultDto.getApp());
        assertEquals("/test/endpoint", resultDto.getUri());
        assertEquals("192.168.1.1", resultDto.getIp());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 30, 0), resultDto.getTimestamp());
    }
}