package ru.practicum.mainservice.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.user.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toUserDto(User entity);

    @Mapping(target = "email", ignore = true)
    UserDto toUserDtoShort(User entity);
}
