package ru.practicum.mainservice.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.exception.CategoryNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        return categoryService.findAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive Long catId) throws CategoryNotFoundException {
        return categoryService.findCategoryById(catId);
    }
}
