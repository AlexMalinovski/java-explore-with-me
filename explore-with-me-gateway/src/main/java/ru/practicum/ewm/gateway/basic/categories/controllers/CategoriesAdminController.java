package ru.practicum.ewm.gateway.basic.categories.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.categories.NewCategoryDto;

import java.security.Principal;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoriesAdminController {

    private final RestTemplate basicServiceRestTemplate;


    /**
     * Добавление новой категории.
     * Эндпоинт: GET /admin/categories
     *
     * @param dto NewCategoryDto
     * @return CategoryDto
     */
    @PostMapping
    public Object createCategory(@RequestBody NewCategoryDto dto) {
        return basicServiceRestTemplate.postForEntity("/admin/categories", dto, CategoryDto.class);
    }

    /**
     * Удаление категории
     * Эндпоинт: DELETE /admin/categories/{catId}
     *
     * @param catId id категории
     * @return no-body object
     */
    @DeleteMapping("/{catId}")
    public Object deleteCategory(@PathVariable long catId) {


        basicServiceRestTemplate.delete("/admin/categories/{catId}", catId);
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
    public Object updateCategory(@PathVariable long catId, @RequestBody CategoryDto dto) {

        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/admin/categories/{catId}", dto, CategoryDto.class, catId));
    }

}
