package ru.practicum.explorewithme.basic.categories.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.categories.models.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long>, CategoriesRepositoryCustom {

}
