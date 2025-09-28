package ru.practicum.mainservice.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
public class UserControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("test@email.com")
                .name("Test User")
                .build();
    }

    @Test
    public void findAll() throws Exception {
        List<UserDto> users = List.of(userDto);
        when(userService.findAllUsers(any(Integer.class), any(Integer.class), anyList()))
                .thenReturn(users);

        mockMvc.perform(get("/admin/users")
                        .param("from", "0")
                        .param("size", "10")
                        .param("ids", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("test@email.com"))
                .andDo(print());

        verify(userService, times(1)).findAllUsers(any(Integer.class), any(Integer.class), anyList());
    }

    @Test
    public void save_ShouldCreateUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        String userSave = "{\n" +
                "    \"email\": \"test@email.com\",\n" +
                "    \"name\": \"Test User\"\n" +
                "}";

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userSave))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andDo(print());

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    public void delete() throws Exception {
        willDoNothing().given(userService).deleteUserById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/users/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(userService, times(1)).deleteUserById(1L);
    }
}