package ru.practicum.explorewithme.basic.events.repositories;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.explorewithme.basic.events.models.Event;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findByWithOffsetAndLimitFetch(BooleanExpression byPredicate, int from, int size);

    List<Event> findByWithOffsetAndLimitFetch(BooleanExpression byPredicate, OrderSpecifier<?> order, int from, int size);
}
