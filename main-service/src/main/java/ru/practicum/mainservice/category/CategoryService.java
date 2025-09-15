package ru.practicum.mainservice.category;

import ru.practicum.mainservice.exception.CategoryNameUniqueException;
import ru.practicum.mainservice.exception.CategoryNotFoundException;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto) throws CategoryNameUniqueException;

    CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException, CategoryNameUniqueException;

    boolean deleteCategory(Long catId);

    CategoryDto findCategoryById(Long catId) throws CategoryNotFoundException;

    List<CategoryDto> findAllCategories(Integer from, Integer size);
}
