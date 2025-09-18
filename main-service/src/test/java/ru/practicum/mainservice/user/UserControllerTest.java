package ru.practicum.mainservice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.mainservice.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link UserControllerAdmin}
 */

@WebMvcTest({UserControllerAdmin.class})
@ContextConfiguration(classes = {UserControllerAdmin.class, UserServiceImpl.class})
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
        String userSave = "{\n" +
                "    \"email\": \"test@email.com\",\n" +
                "    \"name\": \"Test User\"\n" +
                "}";

        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@email.com")
                .name("Test User")
                .build();

        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userSave))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/{id}", "1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(print());
    }
}
