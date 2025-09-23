package ru.practicum.mainservice.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.event.EventRepository;
import ru.practicum.mainservice.exception.CategoryIsRelatedToEventException;
import ru.practicum.mainservice.exception.CategoryNameUniqueException;
import ru.practicum.mainservice.exception.CategoryNotFoundException;
import ru.practicum.mainservice.exception.InvalidCategoryException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) throws CategoryNameUniqueException, InvalidCategoryException {
        log.info("Main-service. createCategory input: name = {}", categoryDto.getName());

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryNameUniqueException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category createdCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));

        log.info("Main-service. createCategory success: id = {}", createdCategory.getId());

        return categoryMapper.toDto(createdCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException, CategoryNameUniqueException, InvalidCategoryException {
        if (categoryDto.getName() == null) {
            throw new InvalidCategoryException("Category name must not be null");
        }

        log.info("Main-service. updateCategory input: id = {}, name = {}", categoryDto.getId(), categoryDto.getName());

        Category existingCategory = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new CategoryNotFoundException("Category with id %s not found".formatted(categoryDto.getId())));

        if (!categoryDto.getName().equals(existingCategory.getName()) && categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryNameUniqueException("Category with name '" + categoryDto.getName() + "' already exists");
        }
        existingCategory.setName(categoryDto.getName());

        Category updatedCategory = categoryRepository.save(existingCategory);

        log.info("Main-service. updateCategory success: id = {}", updatedCategory.getId());

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long catId) throws CategoryIsRelatedToEventException {
        log.info("Main-service. deleteCategory input: id = {}", catId);

        if (eventRepository.existsByCategoryId(catId)) {
            throw new CategoryIsRelatedToEventException("Category is related to event");
        }

        categoryRepository.deleteById(catId);

        log.info("Main-service. deleteCategory success: id = {}", catId);

        return true;
    }

    @Override
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        log.info("Main-service. findAllCategories input: from = {}, size = {}", from, size);

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDto> categories = categoryPage.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        log.info("Main-service. findAllCategories success: size = {}", categories.size());

        return categories;
    }

    @Override
    public CategoryDto findCategoryById(Long catId) throws CategoryNotFoundException {
        log.info("Main-service. findCategoryById input: catId = {}", catId);

        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException("Category with id %s not found".formatted(catId)));

        log.info("Main-service. findCategoryById success: id = {}", category.getId());

        return categoryMapper.toDto(category);
    }

}
