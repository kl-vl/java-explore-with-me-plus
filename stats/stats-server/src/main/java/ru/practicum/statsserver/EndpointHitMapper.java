package ru.practicum.statsserver;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.statsdto.EndpointHitDto;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHitEntity toEntity(EndpointHitDto dto);

    EndpointHitDto toDto(EndpointHitEntity entity);
}
