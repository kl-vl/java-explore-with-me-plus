package ru.practicum.mainservice.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link CategoryControllerPublic}
 */
@AutoConfigureMockMvc
@SpringBootTest
public class CategoryControllerPublicTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    public void getCategories() throws Exception {
        List<CategoryDto> categories = List.of(
                CategoryDto.builder().id(1L).name("Category 1").build(),
                CategoryDto.builder().id(2L).name("Category 2").build()
        );

        when(categoryService.findAllCategories(0, 10))
                .thenReturn(categories);

        mockMvc.perform(get("/categories")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Category 2"))
                .andDo(print());

        verify(categoryService, times(1)).findAllCategories(any(Integer.class), any(Integer.class));
    }

    @Test
    public void getCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = CategoryDto.builder()
                .id(categoryId)
                .name("Test Category")
                .build();

        when(categoryService.findCategoryById(categoryId))
                .thenReturn(categoryDto);


        mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andDo(print());

        verify(categoryService, times(1)).findCategoryById(categoryId);
    }
}
