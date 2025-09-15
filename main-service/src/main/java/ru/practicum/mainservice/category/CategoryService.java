package ru.practicum.mainservice.category;

import ru.practicum.mainservice.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    boolean deleteCategory(Long catId);

    CategoryDto findCategoryById(Long catId) throws CategoryNotFoundException;

    List<CategoryDto> findAllCategories(Integer from, Integer size);
}
