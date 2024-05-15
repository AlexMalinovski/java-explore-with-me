package ru.practicum.explorewithme.basic.service.categories.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.basic.service.categories.models.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category mapToCategory(NewCategoryDto dto);

    CategoryDto mapToCategoryDto(Category category);

    Category mapToCategory(CategoryDto dto);

    List<CategoryDto> mapToCategoryDto(List<Category> list);
}
