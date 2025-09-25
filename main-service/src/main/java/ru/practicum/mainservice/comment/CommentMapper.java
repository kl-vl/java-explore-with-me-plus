package ru.practicum.mainservice.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.CommentDtoShort;
import ru.practicum.mainservice.config.CommonMapperConfiguration;

@Mapper(config = CommonMapperConfiguration.class)
public interface CommentMapper {
    Comment toEntity(CommentDto commentDto);

    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "authorId", source = "author.id")
    CommentDto toDto(Comment entity);

    @Mapping(target = "eventIAnnotation", source = "event.annotation")
    @Mapping(target = "authorName", source = "author.name")
    CommentDtoShort toDtoShort(Comment entity);
}
