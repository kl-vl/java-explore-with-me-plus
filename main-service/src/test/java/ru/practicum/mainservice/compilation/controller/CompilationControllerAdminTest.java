package ru.practicum.mainservice.compilation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.mainservice.compilation.CompilationService;
import ru.practicum.mainservice.compilation.CompilationServiceImpl;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.event.dto.EventDto;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CompilationControllerAdmin}
 */
@WebMvcTest({CompilationControllerAdmin.class})
@ContextConfiguration(classes = {CompilationControllerAdmin.class, CompilationServiceImpl.class})
public class CompilationControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompilationService compilationService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventDto eventDto1;
    private CompilationDto compilationDto;

    @BeforeEach
    void setUp() {
        eventDto1 = EventDto.builder()
                .id(1L)
                .title("Event 1")
                .build();

        EventDto eventDto2 = EventDto.builder()
                .id(2L)
                .title("Event 2")
                .build();

        compilationDto = CompilationDto.builder()
                .id(1L)
                .title("Test Compilation")
                .pinned(true)
                .events(Set.of(eventDto1, eventDto2))
                .build();
    }

    @Test
    public void create() throws Exception {
        CompilationCreateDto compilationCreateDto = CompilationCreateDto.builder()
                .events(Set.of(1L))
                .pinned(true)
                .title("Test Title")
                .build();

        when(compilationService.create(any(CompilationCreateDto.class))).thenReturn(compilationDto);

        mockMvc.perform(post("/admin/compilations")
                        .content(objectMapper.writeValueAsString(compilationCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Compilation"))
                .andDo(print());

        verify(compilationService, times(1)).create(any(CompilationCreateDto.class));
    }

    @Test
    public void delete() throws Exception {
        willDoNothing().given(compilationService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/compilations/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(compilationService, times(1)).delete(1L);
    }

    @Test
    public void update() throws Exception {
        CompilationCreateDto compilationCreateDto = CompilationCreateDto.builder()
                .events(Set.of(1L))
                .pinned(true)
                .title("Updated Title")
                .build();

        CompilationDto updatedDto = CompilationDto.builder()
                .id(1L)
                .title("Updated Title")
                .pinned(true)
                .events(Set.of(eventDto1))
                .build();

        when(compilationService.update(eq(1L), any(CompilationCreateDto.class))).thenReturn(updatedDto);

        mockMvc.perform(patch("/admin/compilations/{id}", "1")
                        .content(objectMapper.writeValueAsString(compilationCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andDo(print());

        verify(compilationService, times(1)).update(eq(1L), any(CompilationCreateDto.class));
    }
}
