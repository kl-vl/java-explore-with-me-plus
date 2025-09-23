package ru.practicum.mainservice.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.practicum.mainservice.category.Category;
import ru.practicum.mainservice.category.CategoryDto;
import ru.practicum.mainservice.config.CommonMapperConfiguration;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventDtoFull;
import ru.practicum.mainservice.location.Location;
import ru.practicum.mainservice.location.LocationDto;
import ru.practicum.mainservice.user.User;
import ru.practicum.mainservice.user.dto.UserDto;

@Mapper(config = CommonMapperConfiguration.class)
public interface EventMapper {

    // EventDto только ID
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToId")
    @Mapping(target = "initiator", source = "initiator", qualifiedByName = "mapUserToId")
    @Mapping(target = "location", source = "location", qualifiedByName = "mapLocationToDto")
    @Mapping(target = "views", ignore = true)
    EventDto toEventDto(Event event);

    // EventDtoFull - DTO
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToDto")
    @Mapping(target = "initiator", source = "initiator", qualifiedByName = "mapUserToDto")
    @Mapping(target = "location", source = "location", qualifiedByName = "mapLocationToDto")
    EventDtoFull toEventFullDto(Event event);

    @Mapping(target = "category", source = "category", qualifiedByName = "mapIdToCategory")
    @Mapping(target = "initiator", source = "initiator", qualifiedByName = "mapIdToUser")
    @Mapping(target = "location", source = "location", qualifiedByName = "mapDtoToLocation")
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    Event toEvent(EventDto eventDto);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    void updateEventFromDto(EventDto eventDto, @MappingTarget Event event);

    @Named("mapCategoryToId")
    default Long mapCategoryToId(Category category) {
        return category != null ? category.getId() : null;
    }

    @Named("mapUserToId")
    default Long mapUserToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("mapCategoryToDto")
    default CategoryDto mapCategoryToDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Named("mapLocationToDto")
    default LocationDto mapLocationToDto(Location location) {
        if (location == null) {
            return null;
        }
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    @Named("mapUserToDto")
    default UserDto mapUserToDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Named("mapIdToCategory")
    default Category mapIdToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return Category.builder().id(categoryId).build();
    }

    @Named("mapIdToUser")
    default User mapIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return User.builder().id(userId).build();
    }

    @Named("mapDtoToLocation")
    default Location mapDtoToLocation(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

}