package ru.practicum.mainservice.compilation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.compilation.CompilationService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/compilations")
                        .param("pinned", "false")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/compilations/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @BeforeEach
    public void setup() {

    }
}
