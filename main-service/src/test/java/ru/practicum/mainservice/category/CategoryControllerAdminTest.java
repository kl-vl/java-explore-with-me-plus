package ru.practicum.mainservice.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CategoryControllerAdmin}
 */
@AutoConfigureMockMvc
@SpringBootTest
public class CategoryControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    public void createCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Category 1")
                .build();
        mockMvc.perform(post("/admin/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andDo(print());
    }

    @Test
    public void updateCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .name("Category 1")
                .build();

        mockMvc.perform(patch("/admin/categories/{0}", "1")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteCategory() throws Exception {
        mockMvc.perform(delete("/admin/categories/{0}", "1"))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andDo(print());
    }
}
