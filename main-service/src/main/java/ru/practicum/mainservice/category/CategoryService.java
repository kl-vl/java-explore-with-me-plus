package ru.practicum.mainservice.category;

import ru.practicum.mainservice.exception.CategoryIsRelatedToEventException;
import ru.practicum.mainservice.exception.CategoryNameUniqueException;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.InvalidCategoryException;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto) throws CategoryNameUniqueException, InvalidCategoryException;

    CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException, CategoryNameUniqueException, InvalidCategoryException;

    boolean deleteCategory(Long catId) throws CategoryIsRelatedToEventException;

    CategoryDto findCategoryById(Long catId) throws CategoryNotFoundException;

    List<CategoryDto> findAllCategories(Integer from, Integer size);
}
