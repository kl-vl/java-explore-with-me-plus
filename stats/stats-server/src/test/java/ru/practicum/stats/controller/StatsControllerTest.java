package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link StatsController}
 */
@WebMvcTest({StatsController.class})
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private StatsService statsService;

    private final LocalDateTime start = LocalDateTime.now().minusDays(1);

    private final EndpointHitDto endpointHitDto = EndpointHitDto.builder()
            .id(1L)
            .app("Test")
            .uri("Test")
            .ip("Test")
            .timestamp(start)
            .build();

    private final List<ViewStatsDto> viewStatsDtoList = List.of(
            ViewStatsDto.builder()
                    .uri("Test1")
                    .app("Test1")
                    .hits(9L)
                    .build(),

            ViewStatsDto.builder()
                    .uri("Test2")
                    .app("Test2")
                    .hits(1L)
                    .build(),

            ViewStatsDto.builder()
                    .uri("Test3")
                    .app("Test3")
                    .hits(7L)
                    .build()
    );


    @Test
    public void getStats() throws Exception {
        when(statsService.getStat(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(List.class),
                any(Boolean.class)
        ))
                .thenReturn(viewStatsDtoList);
        mockMvc.perform(get("/stats")
                        .param("start", "2025-09-11T13:03:51.6031889")
                        .param("end", "2025-09-11T13:03:51.6031889")
                        .param("uris", "")
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].uri").value("Test1"))
                .andExpect(jsonPath("$[1].uri").value("Test2"))
                .andExpect(jsonPath("$[2].uri").value("Test3"))
                .andExpect(jsonPath("$[0].hits").value(9L))
                .andExpect(jsonPath("$[1].hits").value(1L))
                .andExpect(jsonPath("$[2].hits").value(7L))
                .andExpect(jsonPath("$[0].app").value("Test1"))
                .andExpect(jsonPath("$[1].app").value("Test2"))
                .andExpect(jsonPath("$[2].app").value("Test3"))
                .andDo(print());
    }

    @Test
    public void saveHit() throws Exception {
        mockMvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }
}
