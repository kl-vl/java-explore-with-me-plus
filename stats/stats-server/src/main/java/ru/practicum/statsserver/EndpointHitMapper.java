package ru.practicum.statsserver;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Primary;
import ru.practicum.statsdto.EndpointHitDto;

@Mapper(componentModel = "spring")
@Primary
public interface EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHitEntity toEntity(EndpointHitDto dto);

    EndpointHitDto toDto(EndpointHitEntity entity);
}
