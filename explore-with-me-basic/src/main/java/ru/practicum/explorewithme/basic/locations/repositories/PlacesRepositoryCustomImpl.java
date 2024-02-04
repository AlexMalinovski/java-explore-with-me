package ru.practicum.explorewithme.basic.locations.repositories;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.models.QPlace;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PlacesRepositoryCustomImpl implements PlacesRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Place> findByWithOffsetAndLimit(BooleanExpression byFilter, OrderSpecifier<?> order, int from, int size) {
        QPlace place = QPlace.place;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(place)
                .where(byFilter)
                .orderBy(order)
                .limit(size)
                .offset(from)
                .fetch();
    }
}
