package ru.practicum.mainservice.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.event.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    Compilation toEntity(CompilationDto compilationDto);

    @Mapping(target = "events", source = "events", ignore = true)
    Compilation toEntity(CompilationCreateDto compilationCreateDto);

    CompilationDto toDto(Compilation entity);

    List<CompilationDto> toDtoList(List<Compilation> compilations);


}
