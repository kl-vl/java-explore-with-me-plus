package ru.practicum.mainservice.compilation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.mainservice.compilation.CompilationService;
import ru.practicum.mainservice.compilation.CompilationServiceImpl;
import ru.practicum.mainservice.compilation.dto.CompilationCreateDto;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    public void create() throws Exception {
        CompilationCreateDto compilationCreateDto = CompilationCreateDto.builder()
                .events(Set.of(1L))
                .pinned(true)
                .title("3".repeat(4))
                .build();

        mockMvc.perform(post("/admin/compilations")
                        .content(objectMapper.writeValueAsString(compilationCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/compilations/{0}", "1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        CompilationCreateDto compilationCreateDto = CompilationCreateDto.builder()
                .events(Set.of(1L))
                .pinned(true)
                .title("3".repeat(4))
                .build();

        mockMvc.perform(patch("/admin/compilations/{0}", "1")
                        .content(objectMapper.writeValueAsString(compilationCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
