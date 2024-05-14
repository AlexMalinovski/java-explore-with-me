package ru.practicum.ewm.gateway.basic.categories.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/categories")
public class CategoriesPublicController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Получение категорий
     * Эндпоинт: GET /categories
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return List CategoryDto
     */
    @GetMapping
    public Object getCategories(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {


        return basicServiceRestTemplate.getForEntity(
                "/categories?from={from}&size={size}", CategoryDto[].class, from, size);
    }

    /**
     * Получение информации о категории по её идентификатору
     * Эндпоинт: GET /categories/{catId}
     *
     * @param catId id категории
     * @return CategoryDto
     */
    @GetMapping("/{catId}")
    public Object getCategoryById(@PathVariable long catId) {

        return basicServiceRestTemplate.getForEntity("/categories/{catId}", CategoryDto.class, catId);
    }
}
