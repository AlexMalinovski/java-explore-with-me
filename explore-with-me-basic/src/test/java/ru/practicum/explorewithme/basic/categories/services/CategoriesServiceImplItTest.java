package ru.practicum.explorewithme.basic.categories.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.categories.models.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CategoriesServiceImplItTest {

    @Autowired
    private CategoriesService categoriesService;

    @Test
    @Sql("/categories-service-it-test.sql")
    public void updateCategory() {
        Category category = categoriesService.updateCategory(
                Category.builder()
                        .id(1L)
                        .name("newName")
                        .build());

        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("newName", category.getName());
    }

}