package ru.practicum.mainservice.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.event.EventServiceImpl;
import ru.practicum.mainservice.event.dto.EventDto;
import ru.practicum.mainservice.event.enums.EventStateAction;
import ru.practicum.mainservice.location.LocationDto;
import ru.practicum.mainservice.request.RequestServiceImpl;
import ru.practicum.mainservice.request.RequestStatus;
import ru.practicum.mainservice.request.dto.RequestStatusUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link EventControllerPrivate}
 */
@WebMvcTest({EventControllerPrivate.class})
@ContextConfiguration(classes = {EventControllerPrivate.class, EventServiceImpl.class})
public class EventControllerPrivateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventServiceImpl eventService;

    @MockitoBean
    private RequestServiceImpl requestService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createEvent() throws Exception {
        EventDto eventDto = new EventDto();
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

        mockMvc.perform(post("/users/1/events")
                        .content(mapper.writeValueAsString(eventDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }

    @Test
    public void findEventsByUser() throws Exception {
        mockMvc.perform(get("/users/{0}/events", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findEventByUserId() throws Exception {
        mockMvc.perform(get("/users/{0}/events/{1}", "1", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateEventByUserId() throws Exception {
        EventDto eventDto = new EventDto();
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

        mockMvc.perform(patch("/users/{0}/events/{1}", "1", "1")
                        .content(mapper.writeValueAsString(eventDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getRequestsByOwnerOfEvent() throws Exception {
        mockMvc.perform(get("/users/{0}/events/{1}/requests", "1", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateRequests() throws Exception {
        RequestStatusUpdateDto requestStatusUpdateDto = RequestStatusUpdateDto.builder()
                .requestIds(List.of(1L))
                .status(RequestStatus.PENDING)
                .build();

        mockMvc.perform(patch("/users/{0}/events/{1}/requests", "1", "1")
                        .content(mapper.writeValueAsString(requestStatusUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
