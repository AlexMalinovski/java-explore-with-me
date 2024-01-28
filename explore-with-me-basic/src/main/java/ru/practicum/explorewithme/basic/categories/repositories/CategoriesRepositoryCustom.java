package ru.practicum.explorewithme.basic.categories.repositories;

import ru.practicum.explorewithme.basic.categories.models.Category;

import java.util.List;

public interface CategoriesRepositoryCustom {
    List<Category> findAllWithOffsetAndLimit(int from, int size);

}
