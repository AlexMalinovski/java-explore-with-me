package ru.practicum.explorewithme.basic.service.locations.repositories;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.explorewithme.basic.service.locations.models.Place;

import java.util.List;

public interface PlacesRepositoryCustom {
    List<Place> findByWithOffsetAndLimit(BooleanExpression byFilter, OrderSpecifier<?> order, int from, int size);

}
