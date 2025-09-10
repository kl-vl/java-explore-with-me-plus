package ru.practicum.statsserver;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.statsdto.EndpointHitDto;

@Mapper(componentModel = "spring")
@Component
public interface EndpointHitMapper {

    EndpointHitMapper INSTANCE = Mappers.getMapper(EndpointHitMapper.class);

    @Mapping(target = "id", ignore = true)
    EndpointHitEntity toEntity(EndpointHitDto dto);

    EndpointHitDto toDto(EndpointHitEntity entity);
}
