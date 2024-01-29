package ru.practicum.explorewithme.basic.events.repositories;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.models.QEvent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private JPAQuery<Event> getPredicatedEventQueryWithFetch(BooleanExpression byPredicate) {
        QEvent event = QEvent.event;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(event)
                .innerJoin(event.initiator).fetchJoin()
                .innerJoin(event.category).fetchJoin()
                .where(byPredicate);
    }

    @Override
    public List<Event> findByWithOffsetAndLimitFetch(BooleanExpression byPredicate, int from, int size) {
        QEvent event = QEvent.event;
        return getPredicatedEventQueryWithFetch(byPredicate)
                .orderBy(event.id.asc())
                .limit(size)
                .offset(from)
                .fetch();
    }

    @Override
    public List<Event> findByWithOffsetAndLimitFetch(BooleanExpression byPredicate, OrderSpecifier<?> order, int from, int size) {
        if (order == null) {
            return findByWithOffsetAndLimitFetch(byPredicate, from, size);
        }
        return getPredicatedEventQueryWithFetch(byPredicate)
                .orderBy(order)
                .limit(size)
                .offset(from)
                .fetch();
    }
}
