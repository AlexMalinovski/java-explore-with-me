package ru.practicum.explorewithme.basic.events.services;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
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
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.basic.requests.models.Participation;
import ru.practicum.explorewithme.basic.requests.models.QParticipation;
import ru.practicum.explorewithme.basic.requests.repositories.ParticipationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private CategoriesRepository categoryRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private ParticipationMapper participationMapper;

    @Mock
    private StatsClientAdapter statsClientAdapter;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    void createEvent_ifWithId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(Event.builder().id(1L).build()));

        verify(eventRepository, never()).save(any());
    }

    @Test
    void createEvent() {
        Event expected = Event.builder().build();
        when(eventRepository.save(any())).thenReturn(expected);

        var actual = eventService.createEvent(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(eventRepository).save(expected);
    }

    @Test
    void getEvents() {
        List<Event> expected = List.of(Event.builder().build());
        when(eventRepository.findByWithOffsetAndLimitFetch(any(BooleanExpression.class), anyInt(), anyInt())).thenReturn(expected);

        var actual = eventService.getEvents(1L, 1, 2);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(eventRepository).findByWithOffsetAndLimitFetch(any(BooleanExpression.class), eq(1), eq(2));
    }

    @Test
    void getEventById_ifNotFound_thenThrowNotFoundException() {
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getEventById(1L, 2L));

        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
    }

    @Test
    void getEventById() {
        Event expected = Event.builder().build();
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.of(expected));

        var actual = eventService.getEventById(1L, 2L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
    }

    @Test
    void getEventByIdEventId_ifNotFound_thenThrowNotFoundException() {
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getEventById(1L));

        verify(eventRepository).findEventById(1L);
    }

    @Test
    void getEventByIdEventId() {
        Event expected = Event.builder().build();
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(expected));

        var actual = eventService.getEventById(1L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(eventRepository).findEventById(1L);
    }

    @Test
    void updateEvent_user_ifEventNotFound_thenThrowNotFoundException() {
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.updateEvent(1L, 2L, UpdateEventUserRequest.builder().build()));

        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
    }

    @Test
    void updateEvent_user_ifEventDateAnon_thenThrowForbiddenException() {
        Event expected = Event.builder().eventDate(LocalDateTime.now().plusHours(2)).build();
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.of(expected));

        assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, 2L, UpdateEventUserRequest.builder().build()));

        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
    }

    @Test
    void updateEvent_user_ifEventNotPendingOrCancelled_thenThrowForbiddenException() {
        Event expected = Event.builder().eventDate(LocalDateTime.now().plusYears(2)).state(EventState.PUBLISHED).build();
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.of(expected));

        assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, 2L, UpdateEventUserRequest.builder().build()));

        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
    }

    @Test
    void updateEvent_user() {
        Event expected = Event.builder().eventDate(LocalDateTime.now().plusYears(2)).state(EventState.PENDING).build();
        UpdateEventUserRequest request = UpdateEventUserRequest.builder().build();
        when(eventRepository.findByIdAndInitiator_Id(anyLong(), anyLong())).thenReturn(Optional.of(expected));
        when(eventMapper.updateEvent(any(Event.class), any(UpdateEventUserRequest.class))).thenReturn(expected);
        when(eventRepository.save(expected)).thenReturn(expected);

        var actual = eventService.updateEvent(1L, 2L, request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(eventRepository).findByIdAndInitiator_Id(2L, 1L);
        verify(eventMapper).updateEvent(expected, request);
        verify(eventRepository).save(expected);
    }

    @Test
    void updateEvent_admin_ifEventNotFound_thenThrowNotFoundException() {
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.updateEvent(1L, UpdateEventAdminRequest.builder().build()));

        verify(eventRepository).findEventById(1L);
    }

    @Test
    void updateEvent_admin_ifPublishAndInvalidState_thenThrowForbiddenException() {
        UpdateEventAdminRequest request = UpdateEventAdminRequest.builder().stateAction(StateAction.PUBLISH_EVENT).build();
        for (EventState state : EventState.values()) {
            if (state == EventState.PENDING) {
                continue;
            }
            Event expected = Event.builder().state(state).build();
            when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(expected));

            assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, request));
        }

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_admin_ifRejectAndInvalidState_thenThrowForbiddenException() {
        UpdateEventAdminRequest request = UpdateEventAdminRequest.builder().stateAction(StateAction.REJECT_EVENT).build();
        for (EventState state : EventState.values()) {
            if (state != EventState.PUBLISHED) {
                continue;
            }
            Event expected = Event.builder().state(state).build();
            when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(expected));

            assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, request));
        }

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_admin_ifCategoryNotFound_thenThrowForbiddenException() {
        UpdateEventAdminRequest request = UpdateEventAdminRequest.builder().category(2L).build();
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(Event.builder().build()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, request));

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_admin_ifPublicationIsLaterThanOneHourBeforeEventDate_thenThrowForbiddenException() {
        UpdateEventAdminRequest request = UpdateEventAdminRequest.builder().stateAction(StateAction.PUBLISH_EVENT).build();
        Event event = Event.builder().eventDate(LocalDateTime.now().plusMinutes(59)).state(EventState.PENDING).build();
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(event));
        when(eventMapper.updateEvent(any(Event.class), any(UpdateEventAdminRequest.class))).thenReturn(event);

        assertThrows(ForbiddenException.class, () -> eventService.updateEvent(1L, request));

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEvent_admin() {
        UpdateEventAdminRequest request = UpdateEventAdminRequest.builder().title("title").build();
        Event expected = Event.builder().eventDate(LocalDateTime.now().plusMinutes(61)).state(EventState.PENDING).build();
        when(eventRepository.findEventById(anyLong())).thenReturn(Optional.of(expected));
        when(eventMapper.updateEvent(any(Event.class), any(UpdateEventAdminRequest.class))).thenReturn(expected);
        when(eventRepository.save(any(Event.class))).thenReturn(expected);

        var actual = eventService.updateEvent(1L, request);

        verify(eventRepository).findEventById(1L);
        verify(eventMapper).updateEvent(expected, request);
        verify(eventRepository).save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getEventsAdmin() {
        List<Event> expected = List.of(Event.builder().build());
        when(eventRepository.findByWithOffsetAndLimitFetch(any(BooleanExpression.class), anyInt(), anyInt())).thenReturn(expected);

        var actual = eventService.getEventsAdmin(GetEventRequest.builder().build(), 1, 2);

        verify(eventRepository).findByWithOffsetAndLimitFetch(any(BooleanExpression.class), eq(1), eq(2));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getPublishEventById_lookingForPublishedOnly() {
        when(eventRepository.findByIdAndState(anyLong(), any(EventState.class))).thenReturn(Optional.of(Event.builder().build()));
        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(Map.of()));

        eventService.getPublishEventById(1L);

        verify(eventRepository).findByIdAndState(1L, EventState.PUBLISHED);
    }

    @Test
    void getPublishEventById_requestAndAddNumViewsToResults() {
        when(eventRepository.findByIdAndState(anyLong(), any(EventState.class))).thenReturn(Optional.of(Event.builder().id(100L).build()));
        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(Map.of(100L, 5L)));

        Event actual = eventService.getPublishEventById(100L);

        verify(eventRepository).findByIdAndState(eq(100L), any(EventState.class));
        verify(statsClientAdapter).requestHitsForEvents(List.of(100L));
        assertNotNull(actual);
        assertEquals(100L, actual.getId());
        assertEquals(5L, actual.getViews());
    }

    @Test
    void getPublishEventById_ifNotFound_thenThrowNotFoundException() {
        when(eventRepository.findByIdAndState(anyLong(), any(EventState.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getPublishEventById(1L));

        verify(statsClientAdapter, never()).requestHitsForEvents(anyList());
    }


    @Test
    void getEventsPublic_requestAndAddNumViewsToResults() {
        GetEventRequest req = GetEventRequest.builder().build();
        Event founded = Event.builder().id(100L).build();
        when(eventRepository.findByWithOffsetAndLimitFetch(Expressions.TRUE.isTrue(), null, 0, 10))
                .thenReturn(List.of(founded));
        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(Map.of(founded.getId(), 5L)));

        List<Event> actual = eventService.getEventsPublic(req, 0, 10);

        verify(statsClientAdapter).requestHitsForEvents(List.of(founded.getId()));
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(founded.getId(), actual.get(0).getId());
        assertEquals(5L, actual.get(0).getViews());
    }

    @Test
    void getEventsPublic_whenEmptyReq_thenReturnAsIsInDB() {
        GetEventRequest req = GetEventRequest.builder().build();
        List<Event> founded = List.of(Event.builder().id(1L).build(), Event.builder().id(2L).build());
        when(eventRepository.findByWithOffsetAndLimitFetch(Expressions.TRUE.isTrue(), null, 0, 10))
                .thenReturn(founded);
        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(Map.of()));

        List<Event> actual = eventService.getEventsPublic(req, 0, 10);

        assertNotNull(actual);
    }

    @Test
    void getEventsPublic_whenSortByEventDate_thenSortDesc() {
        GetEventRequest req = GetEventRequest.builder().sort(EventSort.EVENT_DATE).build();
        List<Event> founded = List.of(
                Event.builder().id(1L).build(),
                Event.builder().id(2L).build()
        );
        OrderSpecifier<LocalDateTime> orderSpecifier = QEvent.event.eventDate.desc();
        when(eventRepository.findByWithOffsetAndLimitFetch(any(BooleanExpression.class), any(OrderSpecifier.class), anyInt(), anyInt()))
                .thenReturn(founded);
        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(Map.of()));

        List<Event> actual = eventService.getEventsPublic(req, 0, 10);

        verify(eventRepository).findByWithOffsetAndLimitFetch(Expressions.TRUE.isTrue(), orderSpecifier, 0, 10);
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(founded.get(0).getId(), actual.get(0).getId());
        assertEquals(founded.get(1).getId(), actual.get(1).getId());
    }

    @Test
    void getEventsPublic_whenSortByViews_thenSortDesc() {
        GetEventRequest req = GetEventRequest.builder().sort(EventSort.VIEWS).build();
        List<Event> founded = List.of(
                Event.builder().id(1L).build(),
                Event.builder().id(2L).build()
        );
        when(eventRepository.findByWithOffsetAndLimitFetch(Expressions.TRUE.isTrue(), null, 0, 10))
                .thenReturn(founded);

        when(statsClientAdapter.requestHitsForEvents(anyList())).thenReturn(Optional.of(
                Map.of(1L, 10L,
                        2L, 20L)));

        List<Event> actual = eventService.getEventsPublic(req, 0, 10);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(founded.get(1).getId(), actual.get(0).getId());
        assertEquals(founded.get(0).getId(), actual.get(1).getId());
    }


    @Test
    void getEventParticipation() {
        List<Participation> expected = List.of(Participation.builder().build());
        BooleanExpression filter = QParticipation.participation.event.id.eq(2L)
                .and(QParticipation.participation.event.initiator.id.eq(1L));
        when(participationRepository.findAll(any(BooleanExpression.class))).thenReturn(expected);

        List<Participation> actual = eventService.getEventParticipation(1L, 2L);

        verify(participationRepository).findAll(filter);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void updateEventState_supportConfirmOrRejectRequestActions() {
        for (ParticipationState state : ParticipationState.values()) {
            if (state == ParticipationState.REJECTED || state == ParticipationState.CONFIRMED) {
                continue;
            }
            EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder().status(state).build();

            assertThrows(IllegalArgumentException.class, () -> eventService.updateEventState(1L, 2L, request));
        }

        verify(participationRepository, never()).saveAll(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void updateEventState_ifEventNotFound_thenThrowNotFoundException() {
        when(eventRepository.findOne(any(Predicate.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.updateEventState(1L, 2L,
                EventRequestStatusUpdateRequest.builder().status(ParticipationState.REJECTED).build()));

        verify(eventRepository, never()).save(any(Event.class));
        verify(participationRepository, never()).saveAll(any());
    }

    @Test
    void updateEventState_ifParticipationLimit_thenThrowConflictException() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .status(ParticipationState.CONFIRMED)
                .build();
        Event founded = Event.builder().participantLimit(1).confirmedRequests(1).build();
        when(eventRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(founded));

        assertThrows(ConflictException.class, () -> eventService.updateEventState(1L, 2L, request));

        verify(eventRepository, never()).save(any(Event.class));
        verify(participationRepository, never()).saveAll(any());
    }

    @Test
    void updateEventState_ifHasNotPendingRequest_thenThrowForbiddenException() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .requestIds(List.of(1L))
                .status(ParticipationState.REJECTED)
                .build();
        Event foundedEvent = Event.builder().build();
        when(eventRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(foundedEvent));
        when(participationRepository.findAll(any(Predicate.class), any(Sort.class)))
                .thenReturn(List.of(
                        Participation.builder().status(ParticipationState.CONFIRMED).build()));


        assertThrows(ForbiddenException.class, () -> eventService.updateEventState(1L, 2L, request));

        verify(eventRepository, never()).save(any(Event.class));
        verify(participationRepository, never()).saveAll(any());
    }

    @Test
    void updateEventState_ifHasNotExistRequests_thenThrowBadRequestException() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .requestIds(List.of(1L))
                .status(ParticipationState.REJECTED)
                .build();
        Event foundedEvent = Event.builder().build();
        when(eventRepository.findOne(any(Predicate.class))).thenReturn(Optional.of(foundedEvent));
        when(participationRepository.findAll(any(Predicate.class), any(Sort.class)))
                .thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> eventService.updateEventState(1L, 2L, request));

        verify(eventRepository, never()).save(any(Event.class));
        verify(participationRepository, never()).saveAll(any());
    }

}