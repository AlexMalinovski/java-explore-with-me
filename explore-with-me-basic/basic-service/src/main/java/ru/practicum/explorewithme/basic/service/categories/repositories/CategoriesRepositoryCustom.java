package ru.practicum.explorewithme.basic.service.categories.repositories;

import ru.practicum.explorewithme.basic.service.categories.models.Category;

import java.util.List;

public interface CategoriesRepositoryCustom {
    List<Category> findAllWithOffsetAndLimit(int from, int size);

}
