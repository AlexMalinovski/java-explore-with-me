package ru.practicum.explorewithme.basic.requests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exceptions.api.ForbiddenException;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.events.enums.EventState;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.repositories.EventRepository;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.requests.models.Participation;
import ru.practicum.explorewithme.basic.requests.repositories.ParticipationRepository;
import ru.practicum.explorewithme.basic.users.models.User;
import ru.practicum.explorewithme.basic.users.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceImplTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    ParticipationServiceImpl participationService;

    @Test
    void createParticipation_ifEventNotFound_thenThrowNotFoundException() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> participationService.createParticipation(1L, 2L));

        verify(eventRepository).findById(2L);
        verify(participationRepository, never()).save(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createParticipation_ifUserIsEventInitiator_thenThrowForbiddenException() {
        Long userId = 1L;
        Event event = Event.builder().initiator(User.builder().id(userId).build()).build();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        assertThrows(ForbiddenException.class, () -> participationService.createParticipation(userId, 2L));

        verify(eventRepository).findById(2L);
        verify(participationRepository, never()).save(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createParticipation_ifEventNotPublished_thenThrowForbiddenException() {
        for (EventState state : EventState.values()) {
            if (state == EventState.PUBLISHED) {
                continue;
            }

            Event event = Event.builder().state(state).initiator(User.builder().id(1L).build()).build();
            when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

            assertThrows(ForbiddenException.class, () -> participationService.createParticipation(1L, 2L));
        }

        verify(eventRepository, atLeast(EventState.values().length - 1)).findById(2L);
        verify(participationRepository, never()).save(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createParticipation_ifEventRequestLimit_thenThrowForbiddenException() {
        Event event = Event.builder()
                .participantLimit(1)
                .confirmedRequests(1)
                .state(EventState.PUBLISHED)
                .initiator(User.builder().id(1L).build())
                .build();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        assertThrows(ForbiddenException.class, () -> participationService.createParticipation(1L, 2L));


        verify(eventRepository).findById(2L);
        verify(participationRepository, never()).save(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void createParticipation_ifEventNotLimitedRequests_thenAutoConfirmedAndEventRequestsIncrement() {
        Event event = Event.builder()
                .id(2L)
                .participantLimit(0)
                .requestModeration(false)
                .state(EventState.PUBLISHED)
                .initiator(User.builder().id(1L).build())
                .build();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        ArgumentCaptor<Participation> partCaptor = ArgumentCaptor.forClass(Participation.class);

        participationService.createParticipation(3L, event.getId());

        verify(eventRepository).findById(event.getId());
        verify(participationRepository).save(partCaptor.capture());
        verify(eventRepository).save(eventCaptor.capture());

        Participation savedPart = partCaptor.getValue();
        assertEquals(ParticipationState.CONFIRMED, savedPart.getStatus());
        assertEquals(3L, savedPart.getRequester().getId());
        assertEquals(event.getId(), savedPart.getEvent().getId());

        Event savedEvent = eventCaptor.getValue();
        assertEquals(event.getId(), savedEvent.getId());
        assertEquals(1, savedEvent.getConfirmedRequests());
    }

    @Test
    void createParticipation_ifEventLimitedRequests_thenPendingRequest() {
        Event event = Event.builder()
                .id(2L)
                .participantLimit(10)
                .requestModeration(true)
                .state(EventState.PUBLISHED)
                .initiator(User.builder().id(1L).build())
                .build();
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        ArgumentCaptor<Participation> partCaptor = ArgumentCaptor.forClass(Participation.class);

        participationService.createParticipation(3L, event.getId());

        verify(eventRepository).findById(event.getId());
        verify(eventRepository, never()).save(any());
        verify(participationRepository).save(partCaptor.capture());

        Participation savedPart = partCaptor.getValue();
        assertEquals(ParticipationState.PENDING, savedPart.getStatus());
        assertEquals(3L, savedPart.getRequester().getId());
        assertEquals(event.getId(), savedPart.getEvent().getId());
    }

    @Test
    void getUserParticipation_ifUserNotExist_thenThrowNotFoundException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> participationService.getUserParticipation(1L));

        verify(userRepository).existsById(1L);
        verify(participationRepository, never()).findAllByRequester_Id(anyLong());
    }

    @Test
    void getUserParticipation() {
        List<Participation> expected = List.of();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(participationRepository.findAllByRequester_Id(anyLong())).thenReturn(expected);

        var actual = participationService.getUserParticipation(1L);

        verify(userRepository).existsById(1L);
        verify(participationRepository).findAllByRequester_Id(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void cancelParticipation_ifParticipationNotFound_thenThrowNotFoundException() {
        when(participationRepository.findByIdAndRequester_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> participationService.cancelParticipation(2L, 1L));

        verify(participationRepository).findByIdAndRequester_Id(1L, 2L);
        verify(eventRepository, never()).save(any());
        verify(participationRepository, never()).save(any());
    }

    @Test
    void cancelParticipation_ifCancelConfirmed_thenDecrementEventConfirmedRequestsAndCancelParticipation() {
        Participation part = Participation.builder()
                .id(5L)
                .status(ParticipationState.CONFIRMED)
                .event(Event.builder().id(3L).confirmedRequests(1L).build())
                .build();
        when(participationRepository.findByIdAndRequester_Id(anyLong(), anyLong())).thenReturn(Optional.of(part));
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        ArgumentCaptor<Participation> partCaptor = ArgumentCaptor.forClass(Participation.class);

        participationService.cancelParticipation(2L, 1L);

        verify(participationRepository).findByIdAndRequester_Id(1L, 2L);
        verify(eventRepository).save(eventCaptor.capture());
        verify(participationRepository).save(partCaptor.capture());

        Event savedEvent = eventCaptor.getValue();
        assertEquals(0L, savedEvent.getConfirmedRequests());
        assertEquals(part.getEvent().getId(), savedEvent.getId());

        Participation savedPart = partCaptor.getValue();
        assertEquals(ParticipationState.CANCELED, savedPart.getStatus());
        assertEquals(part.getId(), savedPart.getId());
    }

    @Test
    void cancelParticipation_ifCancelNotConfirmed_thenDecrementEventConfirmedRequestsAndCancelParticipation() {
        Participation part = Participation.builder()
                .id(5L)
                .status(ParticipationState.PENDING)
                .event(Event.builder().id(3L).confirmedRequests(1L).build())
                .build();
        when(participationRepository.findByIdAndRequester_Id(anyLong(), anyLong())).thenReturn(Optional.of(part));
        ArgumentCaptor<Participation> partCaptor = ArgumentCaptor.forClass(Participation.class);

        participationService.cancelParticipation(2L, 1L);

        verify(participationRepository).findByIdAndRequester_Id(1L, 2L);
        verify(eventRepository, never()).save(any());
        verify(participationRepository).save(partCaptor.capture());

        Participation savedPart = partCaptor.getValue();
        assertEquals(ParticipationState.CANCELED, savedPart.getStatus());
        assertEquals(part.getId(), savedPart.getId());
    }
}