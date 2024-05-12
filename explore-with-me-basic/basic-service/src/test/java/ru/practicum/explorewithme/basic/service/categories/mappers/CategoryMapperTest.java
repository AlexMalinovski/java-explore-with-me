package ru.practicum.explorewithme.basic.service.categories.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.basic.service.categories.models.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapperImpl();

    @Test
    void mapToCategory_ifSrcNewCategoryDtoNull_thenTargetNull() {
        var actual = mapper.mapToCategory((NewCategoryDto) null);
        assertNull(actual);
    }

    @Test
    void mapToCategory_ifSrcNewCategoryDto() {
        NewCategoryDto expected = NewCategoryDto.builder()
                .name("name")
                .build();

        var actual = mapper.mapToCategory(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertNull(actual.getId());
    }

    @Test
    void mapToCategory_ifSrcCategoryDtoNull_thenTargetNull() {
        var actual = mapper.mapToCategory((CategoryDto) null);
        assertNull(actual);
    }

    @Test
    void mapToCategory_ifSrcCategoryDto() {
        CategoryDto expected = CategoryDto.builder()
                .id(1L)
                .name("name")
                .build();

        var actual = mapper.mapToCategory(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void mapToCategoryDto_ifSrcCategoryNull_thenTargetNull() {
        var actual = mapper.mapToCategoryDto((Category) null);
        assertNull(actual);
    }

    @Test
    void mapToCategoryDto_ifSrcCategory() {
        Category expected = Category.builder()
                .id(1L)
                .name("name")
                .build();

        var actual = mapper.mapToCategoryDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void mapToCategoryDto_ifSrcCategoryListNull_thenTargetNull() {
        var actual = mapper.mapToCategoryDto((List<Category>) null);
        assertNull(actual);
    }

    @Test
    void mapToCategoryDto_ifSrcCategoryList() {
        List<Category> expected = List.of(Category.builder()
                .id(1L)
                .name("name")
                .build());

        var actual = mapper.mapToCategoryDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
    }
}