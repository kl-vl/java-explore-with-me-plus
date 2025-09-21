package ru.practicum.mainservice.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.controller.CompilationRequestParams;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.event.Event;
import ru.practicum.mainservice.event.EventRepository;
import ru.practicum.mainservice.exception.CompilationNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;


    @Override
    @Transactional
    public CompilationDto create(CompilationCreateDto compilationDto) {
        log.info("Main-server. Create compilations {}", compilationDto);
        //нужна ли детальная информация о неправильных events?
        Compilation compilation = compilationMapper.toEntity(compilationDto);
        if (!compilationDto.getEvents().isEmpty()) {
            compilation.setEvents(getEventsByIds(compilationDto.getEvents()));
        }
        Compilation savedCompilation = compilationRepository.save(compilation);

        log.info("Main-server. Create compilation success: id = {}", savedCompilation.getId());
        return compilationMapper.toDto(savedCompilation);
    }

    @Override
    public CompilationDto getById(Long compilationId) throws CompilationNotFoundException {
        log.info("Main-server. Get compilation by id = {}", compilationId);
        Optional<Compilation> optionalCompilation = compilationRepository.findById(compilationId);
        if (optionalCompilation.isEmpty()) {
            throw new CompilationNotFoundException(String.format("Не найдена подборка с id=%d", compilationId));
        }
        Compilation compilation = optionalCompilation.get();
        log.info("Main-server. Get compilation by id success {}", compilation);
        return compilationMapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void delete(Long compilationId) throws CompilationNotFoundException {
        log.info("Main-server. Delete compilation with id = {}", compilationId);
        Optional<Compilation> optCompilation = compilationRepository.findById(compilationId);
        if (optCompilation.isEmpty()) {
            throw new CompilationNotFoundException(String.format("Не найдена подборка с id = %d", compilationId));
        }
        compilationRepository.delete(optCompilation.get());
    }

    @Override
    public Collection<CompilationDto> findAll(CompilationRequestParams params) {
        log.info("Main-server. findAll compilation: {}", params);

        Pageable pageable = PageRequest.of(
                params.getFrom() / params.getSize(),
                params.getSize(),
                Sort.by("id").ascending()
        );
        Page<Compilation> page;
        if (params.getPinned() != null) {
            page = compilationRepository.findAllByPinned(params.getPinned(), pageable);
        } else {
            page = compilationRepository.findAll(pageable);
        }
        log.info("Main-server. findAll success: found {} compilations", page.getNumberOfElements());
        return compilationMapper.toDtoList(page.getContent());
    }

    @Override
    @Transactional
    public CompilationDto update(Long compilationId, CompilationCreateDto compilationCreateDto) throws CompilationNotFoundException {
        log.info("Main-server. Update compilations id={} with {}", compilationId, compilationCreateDto);
        Optional<Compilation> optCompilation = compilationRepository.findById(compilationId);
        if (optCompilation.isEmpty()) {
            throw new CompilationNotFoundException(String.format("Не найдена подборка с id = %d", compilationId));
        }
        Compilation compilation = optCompilation.get();
        if (compilationCreateDto.getPinned() != null) {
            compilation.setPinned(compilationCreateDto.getPinned());
        }
        if (compilationCreateDto.getEvents() != null) {
            compilation.setEvents(getEventsByIds(compilationCreateDto.getEvents()));
        }
        if (compilationCreateDto.getTitle() != null) {
            compilation.setTitle(compilationCreateDto.getTitle());
        }
        Compilation savedCompilation = compilationRepository.save(compilation);
        log.info("Main-server. Update compilations id={} success: {}", compilationId, compilation);
        return compilationMapper.toDto(savedCompilation);
    }

    private Set<Event> getEventsByIds(Set<Long> ids) {
        List<Event> eventList = eventRepository.findAllById(ids);
        if (eventList.size() != ids.size()) {
            throw new IllegalArgumentException("Переданы несуществующие события");
        }
        return eventList.stream().collect(Collectors.toSet());
    }
}