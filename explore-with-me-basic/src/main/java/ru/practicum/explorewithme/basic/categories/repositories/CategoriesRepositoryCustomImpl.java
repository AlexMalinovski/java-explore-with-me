package ru.practicum.explorewithme.basic.categories.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.explorewithme.basic.categories.models.Category;
import ru.practicum.explorewithme.basic.categories.models.QCategory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CategoriesRepositoryCustomImpl implements CategoriesRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Category> findAllWithOffsetAndLimit(int from, int size) {
        QCategory category = QCategory.category;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(category)
                .orderBy(category.id.asc())
                .limit(size)
                .offset(from)
                .fetch();
    }
}
