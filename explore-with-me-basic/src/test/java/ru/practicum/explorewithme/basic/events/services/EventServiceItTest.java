package ru.practicum.explorewithme.basic.events.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EventServiceItTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventMapper eventMapper;


    @Test
    @Sql("/event-service-it-test.sql")
    void updateEventState_confirm() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .status(ParticipationState.CONFIRMED)
                .requestIds(List.of(1L))
                .build();

        var actual = eventService.updateEventState(1L, 1L, request);
        var event = eventService.getEventById(1L);


        assertNotNull(actual);
        assertNotNull(actual.getConfirmedRequests());
        assertEquals(1, actual.getConfirmedRequests().size());
        assertEquals(1L, actual.getConfirmedRequests().get(0).getId());

        assertNotNull(actual.getRejectedRequests());
        assertEquals(0, actual.getRejectedRequests().size());

        assertNotNull(event);
        assertEquals(1L, event.getConfirmedRequests());
    }

    @Test
    @Sql("/event-service-it-test.sql")
    void updateEventState_confirmWithAutoReject() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .status(ParticipationState.CONFIRMED)
                .requestIds(List.of(1L, 2L))
                .build();

        var actual = eventService.updateEventState(1L, 1L, request);
        var event = eventService.getEventById(1L);


        assertNotNull(actual);
        assertNotNull(actual.getConfirmedRequests());
        assertEquals(1, actual.getConfirmedRequests().size());
        assertEquals(1L, actual.getConfirmedRequests().get(0).getId());

        assertNotNull(actual.getRejectedRequests());
        assertEquals(1, actual.getRejectedRequests().size());
        assertEquals(2L, actual.getRejectedRequests().get(0).getId());

        assertNotNull(event);
        assertEquals(1L, event.getConfirmedRequests());
    }

    @Test
    @Sql("/event-service-it-test.sql")
    void updateEventState_reject() {
        EventRequestStatusUpdateRequest request = EventRequestStatusUpdateRequest.builder()
                .status(ParticipationState.REJECTED)
                .requestIds(List.of(1L))
                .build();

        var actual = eventService.updateEventState(1L, 1L, request);
        var event = eventService.getEventById(1L);


        assertNotNull(actual);
        assertNotNull(actual.getConfirmedRequests());
        assertEquals(0, actual.getConfirmedRequests().size());

        assertNotNull(actual.getRejectedRequests());
        assertEquals(1, actual.getRejectedRequests().size());
        assertEquals(1L, actual.getRejectedRequests().get(0).getId());

        assertNotNull(event);
        assertEquals(0L, event.getConfirmedRequests());
    }
}