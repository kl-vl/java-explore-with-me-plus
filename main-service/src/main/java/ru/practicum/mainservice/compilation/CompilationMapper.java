package ru.practicum.mainservice.compilation;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.event.EventMapper;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    Compilation toEntity(CompilationDto compilationDto);

    @BeforeMapping
    default void validate(Compilation compilation) {
        if (compilation.getEvents() == null) {
            compilation.setEvents(new HashSet<>());
        }
    }

    @Mapping(target = "events", source = "events", ignore = true)
    Compilation toEntity(CompilationCreateDto compilationCreateDto);

    CompilationDto toDto(Compilation entity);

    List<CompilationDto> toDtoList(List<Compilation> compilations);


}
