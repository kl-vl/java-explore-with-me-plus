package ru.practicum.mainservice.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.request.dto.RequestDto;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link RequestControllerPrivate}
 */
@WebMvcTest({RequestControllerPrivate.class})
@ContextConfiguration(classes = {RequestControllerPrivate.class, RequestServiceImpl.class})
public class RequestControllerPrivateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestService requestService;

    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = RequestDto.builder()
                .id(1L)
                .event(1L)
                .requester(1L)
                .status(RequestStatus.PENDING)
                .build();
    }


//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private RequestService requestService;
//    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void getParticipationRequest() throws Exception {
        List<RequestDto> requests = List.of(requestDto);
        when(requestService.getCurrentUserRequests(1L)).thenReturn(requests);

        mockMvc.perform(get("/users/{userId}/requests", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].event").value(1L))
                .andExpect(jsonPath("$[0].requester").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andDo(print());

        verify(requestService, times(1)).getCurrentUserRequests(1L);
    }


    @Test
    public void participationRequest() throws Exception {
        when(requestService.createRequest(1L, 1L)).thenReturn(requestDto);

        mockMvc.perform(post("/users/{userId}/requests", 1L)
                        .param("eventId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.event").value(1L))
                .andExpect(jsonPath("$.requester").value(1L))
                .andDo(print());

        verify(requestService, times(1)).createRequest(1L, 1L);
    }

    @Test
    public void cancelParticipationRequest() throws Exception {
        RequestDto canceledRequest = RequestDto.builder()
                .id(1L)
                .event(1L)
                .requester(1L)
                .status(RequestStatus.CANCELED)
                .build();

        when(requestService.cancelRequests(1L, 1L)).thenReturn(canceledRequest);

        mockMvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", "1", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("CANCELED"))
                .andDo(print());

        verify(requestService, times(1)).cancelRequests(1L, 1L);
    }
}
