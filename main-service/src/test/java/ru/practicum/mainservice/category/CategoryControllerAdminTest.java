package ru.practicum.mainservice.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private CategoryDto categoryDto;
    private CategoryDto responseDto;

    @BeforeEach
    void setUp() {
        reset(categoryService);
    }

    @Test
    public void createCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .name("Category 1")
                .build();

        CategoryDto expectedResponse = CategoryDto.builder()
                .id(categoryId)
                .name("Category 1")
                .build();

        when(categoryService.createCategory(any(CategoryDto.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/admin/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Category 1"))
                .andDo(print());

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }

    @Test
    public void updateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Category 1 Updated")
                .build();

        CategoryDto expectedResponse = CategoryDto.builder()
                .id(categoryId)
                .name("Category 1 Updated")
                .build();


        when(categoryService.updateCategory(any(CategoryDto.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(patch("/admin/categories/{id}", 1L, categoryDto)
                        .content(objectMapper.writeValueAsString(expectedResponse))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Category 1 Updated"))
                .andDo(print());

        verify(categoryService, times(1)).updateCategory(any(CategoryDto.class));
    }

    @Test
    public void deleteCategory() throws Exception {
        Long categoryId = 1L;

        when(categoryService.deleteCategory(categoryId))
                .thenReturn(true);

        mockMvc.perform(delete("/admin/categories/{id}", categoryId))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(categoryService, times(1)).deleteCategory(categoryId);
        verifyNoMoreInteractions(categoryService);
    }
}
