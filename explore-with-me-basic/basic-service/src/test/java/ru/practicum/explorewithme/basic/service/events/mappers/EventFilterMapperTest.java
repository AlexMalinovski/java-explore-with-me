package ru.practicum.explorewithme.basic.service.events.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventState;
import ru.practicum.explorewithme.basic.service.locations.repositories.PlacesRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class EventFilterMapperTest {

    @Mock
    private PlacesRepository placesRepository;

    @InjectMocks
    private EventFilterMapperImpl mapper;

    @Test
    void mapToEventFilterUsers() {
        GetEventRequest expected = GetEventRequest.builder()
                .users(List.of(1L))
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.initiator.id = 1", actual.toString());
    }

    @Test
    void mapToEventFilterStates() {
        GetEventRequest expected = GetEventRequest.builder()
                .states(List.of(EventState.PENDING))
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.state = PENDING", actual.toString());
    }

    @Test
    void mapToEventFilterCategories() {
        GetEventRequest expected = GetEventRequest.builder()
                .categories(List.of(1L))
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.category.id = 1", actual.toString());
    }

    @Test
    void mapToEventFilterRangeStart() {
        GetEventRequest expected = GetEventRequest.builder()
                .rangeStart(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.eventDate >= 2020-01-01T00:00", actual.toString());
    }

    @Test
    void mapToEventFilterRangeEnd() {
        GetEventRequest expected = GetEventRequest.builder()
                .rangeEnd(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.eventDate <= 2020-01-01T00:00", actual.toString());
    }

    @Test
    void mapToEventFilterText() {
        GetEventRequest expected = GetEventRequest.builder()
                .text("text")
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && (lower(event.annotation) like %text% || lower(event.description) like %text%)", actual.toString());
    }

    @Test
    void mapToEventFilterPaid() {
        GetEventRequest expected = GetEventRequest.builder()
                .paid(true)
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && event.paid = true", actual.toString());
    }

    @Test
    void mapToEventFilterAvailable() {
        GetEventRequest expected = GetEventRequest.builder()
                .onlyAvailable(true)
                .build();

        var actual = mapper.mapToEventFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && (event.participantLimit = 0 || event.participantLimit - event.confirmedRequests > 0)", actual.toString());
    }

}