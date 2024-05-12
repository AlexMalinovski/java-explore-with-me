package ru.practicum.explorewithme.basic.service.categories.services;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.basic.service.categories.models.Category;

import java.util.List;

public interface CategoriesService {

    @NonNull
    Category createCategory(@NonNull Category category);

    void deleteCategory(long catId);

    @NonNull
    Category updateCategory(@NonNull Category category);

    @NonNull
    List<Category> getCategories(int from, int size);

    @NonNull
    Category getCategoryById(long catId);
}
