package ru.practicum.mainservice.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.event.EventService;
import ru.practicum.mainservice.event.dto.EventDtoFull;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventControllerPublic.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {EventControllerPublic.class, EventService.class})
class EventControllerPublicTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @Test
    void findEventsShouldReturnEventsWhenValidParameters() throws Exception {
        EventDtoFull eventDto = EventDtoFull.builder()
                .id(1L)
                .title("Test Event")
                .annotation("Test Annotation")
                .description("Test Description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .build();

        List<EventDtoFull> events = List.of(eventDto);

        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(events);

        mockMvc.perform(get("/events")
                        .param("text", "test")
                        .param("paid", "true")
                        .param("onlyAvailable", "true")
                        .param("sort", "EVENT_DATE")
                        .param("categories", "1,2,3")
                        .param("rangeStart", "2024-01-01T00:00:00")
                        .param("rangeEnd", "2024-12-31T23:59:59")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Event"))
                .andExpect(jsonPath("$[0].annotation").value("Test Annotation"));
    }

    @Test
    void findEventsShouldReturnEventsWhenOnlyRequiredParameters() throws Exception {
        EventDtoFull eventDto = EventDtoFull.builder()
                .id(2L)
                .title("Another Event")
                .annotation("Another Annotation")
                .build();

        List<EventDtoFull> events = List.of(eventDto);

        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(events);

        mockMvc.perform(get("/events")
                        .param("text", "another"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].title").value("Another Event"));
    }

    @Test
    void findEventsShouldReturnEventsWhenSortByViews() throws Exception {
        EventDtoFull eventDto = EventDtoFull.builder()
                .id(3L)
                .title("Popular Event")
                .views(100L)
                .build();

        List<EventDtoFull> events = List.of(eventDto);

        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(events);

        mockMvc.perform(get("/events")
                        .param("sort", "VIEWS")
                        .param("onlyAvailable", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[0].views").value(100L));
    }

    @Test
    void findEventsShouldReturnEmptyListWhenNoEventsFound() throws Exception {
        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/events")
                        .param("text", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void findEventsShouldHandleInvalidDateParameters() throws Exception {
        mockMvc.perform(get("/events")
                        .param("rangeStart", "invalid-date")
                        .param("rangeEnd", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findEventsShouldHandleInvalidBooleanParameters() throws Exception {
        mockMvc.perform(get("/events")
                        .param("paid", "not-a-boolean")
                        .param("onlyAvailable", "not-a-boolean"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findEventsShouldHandleInvalidSortParameter() throws Exception {
        mockMvc.perform(get("/events")
                        .param("sort", "INVALID_SORT"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findEventsShouldUseDefaultPaginationWhenNoPaginationParams() throws Exception {
        EventDtoFull eventDto = EventDtoFull.builder()
                .id(4L)
                .title("Event with default pagination")
                .build();

        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(List.of(eventDto));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(4L));
    }

    @Test
    void findEventsShouldHandleMultipleCategories() throws Exception {
        EventDtoFull eventDto = EventDtoFull.builder()
                .id(5L)
                .title("Event with categories")
                .build();

        when(eventService.findEvents(any(), any(HttpServletRequest.class)))
                .thenReturn(List.of(eventDto));

        mockMvc.perform(get("/events")
                        .param("categories", "1,2,3,4,5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5L));
    }

    @Test
    public void findEvenById() throws Exception {
        mockMvc.perform(get("/events/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}