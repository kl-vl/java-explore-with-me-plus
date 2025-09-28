package ru.practicum.mainservice.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.event.EventServiceImpl;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.dto.EventFilterAdmin;
import ru.practicum.mainservice.event.enums.EventState;
import ru.practicum.mainservice.event.enums.EventStateAction;
import ru.practicum.mainservice.location.LocationDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link EventControllerAdmin}
 */
@WebMvcTest({EventControllerAdmin.class})
@ContextConfiguration(classes = {EventControllerAdmin.class, EventServiceImpl.class})
public class EventControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private EventServiceImpl eventService;

    @Test
    public void findEvents() throws Exception {
        EventFilterAdmin eventFilterAdmin = EventFilterAdmin.builder()
                .users(List.of(1L))
                .states(List.of(EventState.PENDING))
                .build();

        mockMvc.perform(get("/admin/events")
                        .content(mapper.writeValueAsString(eventFilterAdmin))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateEventById() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(0L);
        eventDto.setAnnotation("a".repeat(23));
        eventDto.setCategory(1L);
        eventDto.setDescription("a".repeat(23));
        eventDto.setEventDate(LocalDateTime.now().minusDays(1));
        eventDto.setInitiator(0L);
        eventDto.setLocation(LocationDto.builder().id(1L).lon(BigDecimal.ONE).lat(BigDecimal.ONE).build());
        eventDto.setPaid(false);
        eventDto.setParticipantLimit(1);
        eventDto.setRequestModeration(false);
        eventDto.setStateAction(EventStateAction.SEND_TO_REVIEW);
        eventDto.setTitle("a".repeat(3));
        eventDto.setViews(0L);

        mockMvc.perform(patch("/admin/events/{eventId}", "1")
                        .content(mapper.writeValueAsString(eventDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
