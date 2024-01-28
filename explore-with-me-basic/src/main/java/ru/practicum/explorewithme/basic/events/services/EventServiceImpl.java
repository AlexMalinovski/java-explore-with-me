package ru.practicum.explorewithme.basic.events.services;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.exceptions.api.ConflictException;
import ru.practicum.exceptions.api.ForbiddenException;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.categories.repositories.CategoriesRepository;
import ru.practicum.explorewithme.basic.common.services.StatsClientAdapter;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.events.enums.EventSort;
import ru.practicum.explorewithme.basic.events.enums.EventState;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.models.QEvent;
import ru.practicum.explorewithme.basic.events.repositories.EventRepository;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.basic.requests.models.Participation;
import ru.practicum.explorewithme.basic.requests.models.QParticipation;
import ru.practicum.explorewithme.basic.requests.repositories.ParticipationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoriesRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final EventMapper eventMapper;
    private final ParticipationMapper participationMapper;
    private final StatsClientAdapter statsClientAdapter;

    private EventRequestStatusUpdateResult rejectParticipation(List<Participation> participation) {
        List<Participation> saved = participationRepository.saveAll(
                participation
                        .stream()
                        .map(p -> p.toBuilder().status(ParticipationState.REJECTED).build())
                        .collect(Collectors.toList()));
        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(participationMapper.toParticipationRequestDto(saved))
                .build();
    }

    private EventRequestStatusUpdateResult confirmParticipationWithLimit(List<Participation> participation,
                                                                         int numToConfirm) {

        List<Participation> updates = new ArrayList<>();
        for (int i = 0; i < participation.size(); i++) {
            ParticipationState newStatus = (i < numToConfirm) ? ParticipationState.CONFIRMED : ParticipationState.REJECTED;
            updates.add(participation.get(i).toBuilder().status(newStatus).build());
        }
        Map<ParticipationState, List<Participation>> saved = participationRepository
                .saveAll(updates)
                .stream()
                .collect(Collectors.groupingBy(Participation::getStatus));

        return EventRequestStatusUpdateResult.builder()
                .rejectedRequests(participationMapper.toParticipationRequestDto(
                        saved.getOrDefault(ParticipationState.REJECTED, new ArrayList<>())))
                .confirmedRequests(participationMapper.toParticipationRequestDto(
                        saved.getOrDefault(ParticipationState.CONFIRMED, new ArrayList<>())))
                .build();
    }

    @Override
    @NonNull
    public Event createEvent(@NonNull Event event) {
        if (event.getId() != null) {
            throw new IllegalArgumentException("Id can be null.");
        }
        return eventRepository.save(event);
    }

    @Override
    @NonNull
    public List<Event> getEvents(long userId, int from, int size) {
        BooleanExpression byInitiatorId = QEvent.event.initiator.id.eq(userId);
        return eventRepository.findByWithOffsetAndLimitFetch(byInitiatorId, from, size);
    }

    @Override
    @NonNull
    public Event getEventById(long userId, long eventId) {
        return eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    @Override
    @NonNull
    public Event getEventById(long eventId) {
        return eventRepository.findEventById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    @Override
    @NonNull
    @Transactional
    public Event updateEvent(long userId, long eventId, @NonNull UpdateEventUserRequest request) {
        Event event = getEventById(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenException("User is not event initiator");
        }
        LocalDateTime now = LocalDateTime.now();
        if (event.getEventDate().minusHours(2).isBefore(now)) {
            throw new ForbiddenException(
                    String.format("Event id=%d: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                            eventId,
                            event.getEventDate().format(DateTimeFormatter.ISO_DATE_TIME)));
        }
        if (!(event.getState() == EventState.PENDING || event.getState() == EventState.CANCELED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        return eventRepository.save(
                eventMapper.updateEvent(event, request));
    }

    @Override
    @NonNull
    @Transactional
    public Event updateEvent(long eventId, @NonNull UpdateEventAdminRequest request) {
        Event event = getEventById(eventId);
        StateAction action = request.getStateAction();
        if (action == StateAction.PUBLISH_EVENT && event.getState() != EventState.PENDING) {
            throw new ForbiddenException("Событие можно публиковать, только если оно в состоянии ожидания публикации.");
        }
        if (action == StateAction.REJECT_EVENT && event.getState() == EventState.PUBLISHED) {
            throw new ForbiddenException("Событие можно отклонить, только если оно еще не опубликовано.");
        }
        Long newCatId = request.getCategory();
        if (newCatId != null && categoryRepository.findById(newCatId).isEmpty()) {
            throw new ForbiddenException(String.format("Category with id='%s' was not found.", newCatId));
        }

        LocalDateTime now = LocalDateTime.now();
        Event updatedEvent = eventMapper.updateEvent(event, request);
        if (action == StateAction.PUBLISH_EVENT) {
            updatedEvent = updatedEvent.toBuilder()
                    .publishedOn(now)
                    .build();
        }

        LocalDateTime publishedOn = updatedEvent.getPublishedOn();
        if (publishedOn != null && updatedEvent.getEventDate().minusHours(1).isBefore(publishedOn)) {
            throw new ForbiddenException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.");
        }
        return eventRepository.save(updatedEvent);
    }

    @Override
    @NonNull
    public List<Event> getEventsAdmin(@NonNull GetEventRequest req, int from, int size) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        filter = req.getUsers() != null ? filter.and(QEvent.event.initiator.id.in(req.getUsers())) : filter;
        filter = req.getStates() != null ? filter.and(QEvent.event.state.in(req.getStates())) : filter;
        filter = req.getCategories() != null ? filter.and(QEvent.event.category.id.in(req.getCategories())) : filter;
        filter = req.getRangeStart() != null ? filter.and(QEvent.event.eventDate.goe(req.getRangeStart())) : filter;
        filter = req.getRangeEnd() != null ? filter.and(QEvent.event.eventDate.loe(req.getRangeEnd())) : filter;

        return eventRepository.findByWithOffsetAndLimitFetch(filter, from, size);
    }

    @Override
    @NonNull
    public Event getPublishEventById(long eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        final Map<Long, Long> eventViews = statsClientAdapter.requestHitsForEvents(List.of(eventId))
                .orElseThrow(() -> new IllegalStateException("Statistics server is unavailable"));

        return event.toBuilder()
                .views(eventViews.getOrDefault(eventId, 0L))
                .build();
    }

    @NonNull
    @Override
    public List<Event> getEventsPublic(@NonNull GetEventRequest req, int from, int size) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        filter = req.getText() != null ? filter.and(
                QEvent.event.annotation.lower().like(req.getText())
                        .or(QEvent.event.description.lower().like(req.getText())))
                : filter;
        filter = req.getPaid() != null ? filter.and(QEvent.event.paid.eq(req.getPaid())) : filter;
        filter = req.getCategories() != null ? filter.and(QEvent.event.category.id.in(req.getCategories())) : filter;
        filter = req.getRangeStart() != null ? filter.and(QEvent.event.eventDate.goe(req.getRangeStart())) : filter;
        filter = req.getRangeEnd() != null ? filter.and(QEvent.event.eventDate.loe(req.getRangeEnd())) : filter;

        if (req.getOnlyAvailable() != null && req.getOnlyAvailable()) {
            filter = filter.and(
                    QEvent.event.participantLimit.eq(0)
                            .or(QEvent.event.participantLimit.subtract(QEvent.event.confirmedRequests).gt(0)));
        }
        OrderSpecifier<LocalDateTime> orderSpecifier = null;
        if (req.getSort() == EventSort.EVENT_DATE) {
            orderSpecifier = QEvent.event.eventDate.desc();
        }

        List<Event> events = eventRepository.findByWithOffsetAndLimitFetch(filter, orderSpecifier, from, size);
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        final Map<Long, Long> views = statsClientAdapter.requestHitsForEvents(eventIds)
                .orElseThrow(() -> new IllegalStateException("Statistics server is unavailable"));
        Stream<Event> eventStream = events.stream()
                .map(e -> e.toBuilder().views(views.getOrDefault(e.getId(), 0L)).build());
        if (req.getSort() == EventSort.VIEWS) {
            eventStream = eventStream.sorted(Comparator.comparingLong(Event::getViews).reversed());
        }
        return eventStream.collect(Collectors.toList());
    }


    @Override
    @NonNull
    public List<Participation> getEventParticipation(long userId, long eventId) {
        BooleanExpression byEventId = QParticipation.participation.event.id.eq(eventId);
        BooleanExpression byEventInitiatorId = QParticipation.participation.event.initiator.id.eq(userId);

        Iterable<Participation> participation = participationRepository.findAll(byEventId.and(byEventInitiatorId));
        return StreamSupport.stream(participation.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional
    public EventRequestStatusUpdateResult updateEventState(long userId, long eventId, @NonNull EventRequestStatusUpdateRequest request) {
        if (!(request.getStatus() == ParticipationState.REJECTED || request.getStatus() == ParticipationState.CONFIRMED)) {
            throw new IllegalArgumentException("Unsupported status");
        }

        BooleanExpression byEventId = QEvent.event.id.eq(eventId);
        BooleanExpression byEventInitiator = QEvent.event.initiator.id.eq(userId);
        BooleanExpression byEventState = QEvent.event.state.eq(EventState.PUBLISHED);
        Event event = eventRepository.findOne(byEventId.and(byEventInitiator).and(byEventState))
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        long confirmedInEvent = event.getConfirmedRequests();
        if (request.getStatus() == ParticipationState.CONFIRMED
                && confirmedInEvent >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }

        BooleanExpression byParticipationIdIn = QParticipation.participation.id.in(request.getRequestIds());
        List<Participation> participation = StreamSupport
                .stream(participationRepository.findAll(
                                byParticipationIdIn,
                                Sort.by(Sort.Direction.ASC, "created"))
                        .spliterator(), false)
                .peek(p -> {
                    if (p.getStatus() != ParticipationState.PENDING) {
                        throw new ForbiddenException("Requests must be have status PENDING");
                    }
                })
                .collect(Collectors.toList());
        if (participation.size() != request.getRequestIds().size()) {
            throw new BadRequestException("Requests must be exists");
        }

        if (request.getStatus() == ParticipationState.REJECTED) {
            return rejectParticipation(participation);
        }

        int numToConfirm = event.getParticipantLimit() == 0 ?
                participation.size() :
                Math.min(event.getParticipantLimit() - (int) confirmedInEvent, participation.size());
        if (numToConfirm > 0) {
            Event updateEvent = event.toBuilder()
                    .confirmedRequests(confirmedInEvent + numToConfirm)
                    .build();
            eventRepository.save(updateEvent);
        }
        return confirmParticipationWithLimit(participation, numToConfirm);
    }
}
