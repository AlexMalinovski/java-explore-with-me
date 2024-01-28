package ru.practicum.explorewithme.basic.categories.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CategoriesRepositoryTest {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Test
    @Sql("/categories-repository-it-test.sql")
    void findAllWithOffsetAndLimit() {
        var actual = categoriesRepository.findAllWithOffsetAndLimit(1, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(2L, actual.get(0).getId());
    }

}