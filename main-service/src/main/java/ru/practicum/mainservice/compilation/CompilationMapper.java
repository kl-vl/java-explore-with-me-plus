package ru.practicum.mainservice.compilation;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.config.CommonMapperConfiguration;
import ru.practicum.mainservice.event.EventMapper;

import java.util.HashSet;
import java.util.List;

@Mapper(uses = EventMapper.class, config = CommonMapperConfiguration.class)
public interface CompilationMapper {
    @BeforeMapping()
    default void validate(Compilation compilation) {
        if ((compilation != null) && (compilation.getEvents() == null)) {
            compilation.setEvents(new HashSet<>());
        }
    }

    @BeforeMapping()
    default void validate(CompilationCreateDto compilation) {
        if ((compilation != null) && (compilation.getEvents() == null)) {
            compilation.setEvents(new HashSet<>());
        }
        if ((compilation != null) && (compilation.getPinned() == null)) {
            compilation.setPinned(false);
        }
    }

    Compilation toEntity(CompilationDto compilationDto);

    @Mapping(target = "events", source = "events", ignore = true)
    Compilation toEntity(CompilationCreateDto compilationCreateDto);

    CompilationDto toDto(Compilation entity);

    List<CompilationDto> toDtoList(List<Compilation> compilations);


}
