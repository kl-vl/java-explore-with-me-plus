package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.model.EndpointHitEntity;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHitEntity toEntity(EndpointHitDto dto);

    EndpointHitDto toDto(EndpointHitEntity entity);
}
