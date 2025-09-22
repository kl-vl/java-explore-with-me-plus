package ru.practicum.mainservice.compilation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.compilation.CompilationService;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.CompilationRequestParams;
import ru.practicum.mainservice.exception.CompilationNotFoundException;

import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationControllerPublic {
    private final CompilationService compilationService;

    @GetMapping
    Collection<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        CompilationRequestParams params = CompilationRequestParams.builder()
                .pinned(pinned)
                .from(from)
                .size(size)
                .build();
        return compilationService.findAll(params);
    }

    @GetMapping(path = "/{compilationId}")
    public CompilationDto getById(@Valid @PathVariable Long compilationId) throws CompilationNotFoundException {
        return compilationService.getById(compilationId);
    }

}
