package ru.practicum.mainservice.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link UserController}
 */
@WebMvcTest({UserController.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .param("from", "0")
                        .param("size", "10")
                        .param("ids", ""))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void save() throws Exception {
        String userSave = """
                {
                    "email": "",
                    "name": ""
                }""";

        mockMvc.perform(post("/admin/users")
                        .content(userSave)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/{0}", "0"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(print());
    }

    @BeforeEach
    public void setup() {

    }
}
