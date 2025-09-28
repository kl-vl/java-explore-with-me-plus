package ru.practicum.mainservice.compilation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.compilation.CompilationService;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.CompilationRequestParams;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CompilationControllerPublic}
 */
@AutoConfigureMockMvc
@SpringBootTest
public class CompilationControllerPublicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompilationService compilationService;

    private CompilationDto compilationDto;

    @BeforeEach
    void setUp() {
        compilationDto = CompilationDto.builder()
                .id(1L)
                .title("Test Compilation")
                .pinned(true)
                .events(Set.of())
                .build();
    }

    @Test
    public void findAll() throws Exception {
        List<CompilationDto> compilations = List.of(compilationDto);
        CompilationRequestParams params = CompilationRequestParams.builder()
                .pinned(true)
                .from(0)
                .size(10)
                .build();

        when(compilationService.findAll(any(CompilationRequestParams.class))).thenReturn(compilations);

        mockMvc.perform(get("/compilations")
                        .param("pinned", "true")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Compilation"))
                .andDo(print());

        verify(compilationService, times(1)).findAll(any(CompilationRequestParams.class));
    }

    @Test
    public void getById() throws Exception {
        when(compilationService.getById(1L)).thenReturn(compilationDto);

        mockMvc.perform(get("/compilations/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Compilation"))
                .andExpect(jsonPath("$.pinned").value(true))
                .andDo(print());

        verify(compilationService, times(1)).getById(1L);
    }

}
