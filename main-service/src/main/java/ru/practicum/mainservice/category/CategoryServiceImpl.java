package ru.practicum.mainservice.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.exception.CategoryNameUniqueException;
import ru.practicum.mainservice.exception.CategoryNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) throws CategoryNameUniqueException {
        log.info("Main-server. createCategory input: name = {}", categoryDto.getName());

        if (categoryDto.getName() != null && categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryNameUniqueException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category createdCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));

        log.info("Main-server. createCategory success: id = {}", createdCategory.getId());

        return categoryMapper.toDto(createdCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) throws CategoryNotFoundException, CategoryNameUniqueException {
        log.info("Main-server. updateCategory input: id = {}, name = {}", categoryDto.getId(), categoryDto.getName());


        Category existingCategory = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new CategoryNotFoundException("Category with id %s not found".formatted(categoryDto.getId())));

        if (categoryDto.getName() != null &&
                !categoryDto.getName().equals(existingCategory.getName()) &&
                categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryNameUniqueException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        if (categoryDto.getName() != null) {
            existingCategory.setName(categoryDto.getName());
        }

        Category updatedCategory = categoryRepository.save(existingCategory);

        log.info("Main-server. updateCategory success: id = {}", updatedCategory.getId());

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long catId) {
        log.info("Main-server. deleteCategory input: id = {}", catId);

        categoryRepository.deleteById(catId);

        log.info("Main-server. deleteCategory success: id = {}", catId);

        return true;
    }

    @Override
    public List<CategoryDto> findAllCategories(Integer from, Integer size) {
        log.info("Main-server. findAllCategories input: from = {}, size = {}", from, size);

        //List<Category> allCategories = categoryRepository.findAll().subList(from, from + size);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryDto> categories = categoryPage.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        log.info("Main-server. findAllCategories success: size = {}", categories.size());

        return categories;
    }

    @Override
    public CategoryDto findCategoryById(Long catId) throws CategoryNotFoundException {
        log.info("Main-server. findCategoryById input: catId = {}", catId);

        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException("Category with id %s not found".formatted(catId)));

        log.info("Main-server. findCategoryById success: id = {}", category.getId());

        return categoryMapper.toDto(category);
    }

}
