package ru.practicum.explorewithme.basic.categories.services;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.categories.models.Category;
import ru.practicum.explorewithme.basic.categories.repositories.CategoriesRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final String notFoundMsg = "Category with id='%s' was not found.";
    private final CategoriesRepository categoriesRepository;

    @Override
    @NonNull
    @Transactional
    public Category createCategory(@NonNull Category category) {
        if (category.getId() != null) {
            throw new IllegalArgumentException("id can be null.");
        }
        return categoriesRepository.save(category);
    }

    @Override
    public void deleteCategory(long catId) {
        if (catId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, catId));
        }
        categoriesRepository.deleteById(catId);
    }

    @Override
    @NonNull
    @Transactional
    public Category updateCategory(@NonNull Category category) {
        long categoryId = Optional.ofNullable(category.getId())
                .orElseThrow(() -> new IllegalArgumentException("Category.id can't be null"));
        if (categoryId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, categoryId));
        }

        return categoriesRepository.save(category);
    }

    @Override
    @NonNull
    public List<Category> getCategories(int from, int size) {
        return categoriesRepository.findAllWithOffsetAndLimit(from, size);
    }

    @Override
    @NonNull
    public Category getCategoryById(long catId) {
        if (catId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, catId));
        }
        return categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format(notFoundMsg, catId)));
    }
}
