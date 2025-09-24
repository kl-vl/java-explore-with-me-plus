package ru.practicum.mainservice.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.mainservice.handler.ErrorHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    public void getParticipationRequest() throws Exception {
        mockMvc.perform(get("/users/{userId}/requests", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void participationRequest() throws Exception {
        mockMvc.perform(post("/users/{userId}/requests", "1")
                        .param("eventId", "1"))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }

    @Test
    public void cancelParticipationRequest() throws Exception {
        mockMvc.perform(patch("/users/{userId}/requests/{requestId}/cancel", "1", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
