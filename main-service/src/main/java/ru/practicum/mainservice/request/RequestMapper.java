package ru.practicum.mainservice.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.mainservice.config.CommonMapperConfiguration;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.request.dto.RequestDto;
import ru.practicum.mainservice.user.User;

import java.util.List;

@Mapper(config = CommonMapperConfiguration.class)
public interface RequestMapper {

    @Mapping(target = "event", source = "event", qualifiedByName = "mapEventToId")
    @Mapping(target = "requester", source = "requester", qualifiedByName = "mapUserToId")
    RequestDto toDto(Request request);

    @Mapping(target = "event", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "status", ignore = true)
    Request toEntity(RequestDto requestDto);

    // Маппинг для списков
    List<RequestDto> toDtoList(List<Request> requests);

    // Кастомные методы маппинга
    @Named("mapEventToId")
    default Long mapEventToId(Event event) {
        return event != null ? event.getId() : null;
    }

    @Named("mapUserToId")
    default Long mapUserToId(User user) {
        return user != null ? user.getId() : null;
    }

    // Обратные методы (если понадобятся)
    @Named("mapIdToEvent")
    default Event mapIdToEvent(Long eventId) {
        if (eventId == null) {
            return null;
        }
        return Event.builder().id(eventId).build();
    }

    @Named("mapIdToUser")
    default User mapIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return User.builder().id(userId).build();
    }
}