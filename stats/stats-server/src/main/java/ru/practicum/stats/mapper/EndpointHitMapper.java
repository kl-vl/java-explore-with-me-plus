package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.config.CommonMapperConfiguration;
import ru.practicum.stats.model.EndpointHitEntity;

@Mapper(config = CommonMapperConfiguration.class)
public interface EndpointHitMapper {

    @Mapping(target = "id", ignore = true)
    EndpointHitEntity toEntity(EndpointHitDto dto);

    EndpointHitDto toDto(EndpointHitEntity entity);
}
