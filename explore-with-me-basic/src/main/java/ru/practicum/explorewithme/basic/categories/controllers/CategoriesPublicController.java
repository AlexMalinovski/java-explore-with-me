package ru.practicum.explorewithme.basic.categories.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.categories.dto.CategoryDto;
import ru.practicum.explorewithme.basic.categories.mappers.CategoryMapper;
import ru.practicum.explorewithme.basic.categories.models.Category;
import ru.practicum.explorewithme.basic.categories.services.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Validated
public class CategoriesPublicController {

    private final CategoriesService categoriesService;
    private final CategoryMapper categoryMapper;

    /**
     * Получение категорий
     * Эндпоинт: GET /categories
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return List CategoryDto
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        List<Category> categories = categoriesService.getCategories(from, size);

        return ResponseEntity.ok(categoryMapper.mapToCategoryDto(categories));
    }

    /**
     * Получение информации о категории по её идентификатору
     * Эндпоинт: GET /categories/{catId}
     *
     * @param catId id категории
     * @return CategoryDto
     */
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable long catId) {
        Category category = categoriesService.getCategoryById(catId);
        return ResponseEntity.ok(categoryMapper.mapToCategoryDto(category));
    }
}
