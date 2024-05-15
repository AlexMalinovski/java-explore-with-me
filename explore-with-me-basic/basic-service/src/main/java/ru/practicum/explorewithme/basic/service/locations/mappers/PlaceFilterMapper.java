package ru.practicum.explorewithme.basic.service.locations.mappers;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import org.mapstruct.Mapper;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceSort;
import ru.practicum.explorewithme.basic.service.locations.models.QPlace;

@Mapper(componentModel = "spring")
public abstract class PlaceFilterMapper {

    public BooleanExpression toPlaceFilter(PlaceFilterRequest req) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        if (req == null) {
            return filter;
        }
        if (req.getName() != null) {
            filter = filter.and(QPlace.place.name.likeIgnoreCase("%" + req.getName() + "%"));
        }
        if (req.getDescription() != null) {
            filter = filter.and(QPlace.place.description.likeIgnoreCase("%" + req.getDescription() + "%"));
        }
        if (req.getStatuses() != null) {
            filter = filter.and(QPlace.place.status.in(req.getStatuses()));
        }
        if (req.getArea() != null) {
            BooleanTemplate byDistance = Expressions
                    .booleanTemplate("function('earth_distance', {0}, {1}, {2}, {3}) <= {4}",
                            req.getArea().getLat(), req.getArea().getLon(),
                            QPlace.place.location.lat, QPlace.place.location.lon,
                            req.getArea().getRadius());
            filter = filter.and(byDistance);
        }

        return filter;
    }

    public OrderSpecifier<?> toPlaceOrder(PlaceFilterRequest req) {
        if (req == null || req.getOrderBy() == null || req.getOrderBy() == PlaceSort.DEFAULT) {
            return QPlace.place.id.asc();
        }
        if (req.getOrderBy() == PlaceSort.NAME_ASC) {
            return QPlace.place.name.asc();
        }
        if (req.getOrderBy() == PlaceSort.NAME_DESC) {
            return QPlace.place.name.desc();
        }
        if (req.getOrderBy() == PlaceSort.STATUS_ASC) {
            return QPlace.place.status.asc();
        }
        if (req.getOrderBy() == PlaceSort.STATUS_DESC) {
            return QPlace.place.status.desc();
        }

        return QPlace.place.id.asc();
    }
}
