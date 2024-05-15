package ru.practicum.explorewithme.basic.service.categories.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.basic.service.categories.mappers.CategoryMapper;
import ru.practicum.explorewithme.basic.service.categories.models.Category;
import ru.practicum.explorewithme.basic.service.categories.services.CategoriesService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
@Validated
public class CategoriesAdminController {

    private final CategoriesService categoriesService;
    private final CategoryMapper categoryMapper;

    /**
     * Добавление новой категории.
     * Эндпоинт: GET /admin/categories
     *
     * @param dto NewCategoryDto
     * @return CategoryDto
     */
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid NewCategoryDto dto) {
        Category created = categoriesService.createCategory(categoryMapper.mapToCategory(dto));
        return new ResponseEntity<>(categoryMapper.mapToCategoryDto(created), HttpStatus.CREATED);
    }

    /**
     * Удаление категории
     * Эндпоинт: DELETE /admin/categories/{catId}
     *
     * @param catId id категории
     * @return no-body object
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long catId) {
        categoriesService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Изменение категории.
     * Эндпоинт: PATH /admin/categories/{catId}
     *
     * @param catId id категории
     * @param dto   CategoryDto
     * @return CategoryDto
     */
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable long catId, @RequestBody @Valid CategoryDto dto) {
        Category updated = categoriesService.updateCategory(
                categoryMapper.mapToCategory(dto)
                        .toBuilder()
                        .id(catId)
                        .build());
        return ResponseEntity.ok(categoryMapper.mapToCategoryDto(updated));
    }

}
