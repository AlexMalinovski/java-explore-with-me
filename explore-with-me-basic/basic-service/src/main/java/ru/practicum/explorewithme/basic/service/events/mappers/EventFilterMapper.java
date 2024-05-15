package ru.practicum.explorewithme.basic.service.events.mappers;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.service.events.models.QEvent;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.service.locations.models.Place;
import ru.practicum.explorewithme.basic.service.locations.models.QPlace;
import ru.practicum.explorewithme.basic.service.locations.repositories.PlacesRepository;

@Mapper(componentModel = "spring")
public abstract class EventFilterMapper {

    @Autowired
    private PlacesRepository placesRepository;

    public BooleanExpression mapToEventFilter(GetEventRequest req) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        filter = req.getUsers() != null ? filter.and(QEvent.event.initiator.id.in(req.getUsers())) : filter;
        filter = req.getStates() != null ? filter.and(QEvent.event.state.in(req.getStates())) : filter;
        filter = req.getCategories() != null ? filter.and(QEvent.event.category.id.in(req.getCategories())) : filter;
        filter = req.getRangeStart() != null ? filter.and(QEvent.event.eventDate.goe(req.getRangeStart())) : filter;
        filter = req.getRangeEnd() != null ? filter.and(QEvent.event.eventDate.loe(req.getRangeEnd())) : filter;
        filter = req.getPaid() != null ? filter.and(QEvent.event.paid.eq(req.getPaid())) : filter;

        filter = req.getText() != null ? filter.and(
                QEvent.event.annotation.likeIgnoreCase("%" + req.getText() + "%")
                        .or(QEvent.event.description.likeIgnoreCase("%" + req.getText() + "%")))
                : filter;

        if (req.getOnlyAvailable() != null && req.getOnlyAvailable()) {
            filter = filter.and(
                    QEvent.event.participantLimit.eq(0)
                            .or(QEvent.event.participantLimit.subtract(QEvent.event.confirmedRequests).gt(0)));
        }

        if (req.getPlaceId() != null) {
            if (req.getPlaceId() <= 0) {
                throw new BadRequestException("Place not found");
            }
            Predicate byIdAndPublished = QPlace.place.id.eq(req.getPlaceId()).and(
                    QPlace.place.status.eq(PlaceStatus.PUBLISHED));
            Place place = placesRepository.findOne(byIdAndPublished)
                    .orElseThrow(() -> new BadRequestException("Place not found"));
            filter = filter.and(Expressions
                    .booleanTemplate("function('earth_distance', {0}, {1}, {2}, {3}) <= {4}",
                            place.getLocation().getLat(), place.getLocation().getLon(),
                            QEvent.event.location.lat, QEvent.event.location.lon,
                            place.getRadius()));
        }

        return filter;
    }
}
