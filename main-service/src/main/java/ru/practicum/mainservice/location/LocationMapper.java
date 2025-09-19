package ru.practicum.mainservice.location;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "id", ignore = true)
    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
