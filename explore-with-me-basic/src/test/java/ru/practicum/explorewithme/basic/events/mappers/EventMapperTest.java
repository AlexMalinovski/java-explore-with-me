package ru.practicum.explorewithme.basic.events.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.basic.categories.mappers.CategoryMapper;
import ru.practicum.explorewithme.basic.categories.models.Category;
import ru.practicum.explorewithme.basic.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.events.enums.EventSort;
import ru.practicum.explorewithme.basic.events.enums.EventState;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.models.embeddable.Location;
import ru.practicum.explorewithme.basic.users.mappers.UserMapper;
import ru.practicum.explorewithme.basic.users.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventMapperTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private UserMapper userMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private EventMapperImpl mapper;

    private Event getEvent() {
        return Event.builder()
                .id(1L)
                .title("title")
                .annotation("anno")
                .category(Category.builder().build())
                .createdOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .description("description")
                .eventDate(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                .initiator(User.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(10)
                .publishedOn(LocalDateTime.of(2020, 1, 1, 0, 0, 0))
                .requestModeration(false)
                .state(EventState.PUBLISHED)
                .confirmedRequests(10L)
                .views(20L)
                .build();
    }

    private NewEventDto getNewEventDto() {
        return NewEventDto.builder()
                .title("title")
                .annotation("anno")
                .category(1L)
                .description("description")
                .eventDate("2025-01-01 00:00:00")
                .location(LocationDto.builder().build())
                .paid(true)
                .participantLimit(10)
                .requestModeration(false)
                .build();
    }

    @Test
    void mapToEvent_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToEvent((NewEventDto) null);
        assertNull(actual);
    }

    @Test
    void mapToEvent() {
        NewEventDto expected = getNewEventDto();

        var actual = mapper.mapToEvent(expected);

        assertNotNull(actual);
        assertNotNull(actual.getCategory());
        assertEquals(expected.getCategory(), actual.getCategory().getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAnnotation(), actual.getAnnotation());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getEventDate(), actual.getEventDate().format(DATE_FORMATTER));
        verify(locationMapper).mapToLocation(expected.getLocation());
        assertEquals(expected.isPaid(), actual.getPaid());
        assertEquals(expected.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expected.isRequestModeration(), actual.getRequestModeration());
    }

    @Test
    void mapToEvent_setDefaults() {
        NewEventDto expected = NewEventDto.builder()
                .title("title")
                .annotation("anno")
                .category(1L)
                .description("description")
                .eventDate("2025-01-01 00:00:00")
                .location(LocationDto.builder().build())
                .build();

        var actual = mapper.mapToEvent(expected);

        assertNotNull(actual);
        assertEquals(Boolean.FALSE, actual.getPaid());
        assertEquals(0, actual.getParticipantLimit());
        assertEquals(true, actual.getRequestModeration());
    }

    @Test
    void mapToEventFullDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToEventFullDto((Event) null);
        assertNull(actual);
    }

    @Test
    void mapToEventFullDto() {
        Event expected = getEvent();

        var actual = mapper.mapToEventFullDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAnnotation(), actual.getAnnotation());
        verify(categoryMapper).mapToCategoryDto(expected.getCategory());
        assertEquals(expected.getConfirmedRequests(), actual.getConfirmedRequests());
        assertEquals(expected.getCreatedOn(), LocalDateTime.parse(actual.getCreatedOn(), DATE_FORMATTER));
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getEventDate(), LocalDateTime.parse(actual.getEventDate(), DATE_FORMATTER));
        verify(userMapper).mapToUserShortDto(expected.getInitiator());
        verify(locationMapper).mapToLocationDto(expected.getLocation());
        assertEquals(expected.getPaid(), actual.isPaid());
        assertEquals(expected.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expected.getPublishedOn(), LocalDateTime.parse(actual.getPublishedOn(), DATE_FORMATTER));
        assertEquals(expected.getRequestModeration(), actual.isRequestModeration());
        assertEquals(expected.getState().name(), actual.getState());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getViews(), actual.getViews());
    }

    @Test
    void mapToEventFullDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapToEventFullDto((List<Event>) null);
        assertNull(actual);
    }

    @Test
    void mapToEventFullDto_ifSrcList() {
        List<Event> expected = List.of(getEvent());

        var actual = mapper.mapToEventFullDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getAnnotation(), actual.get(0).getAnnotation());
        verify(categoryMapper).mapToCategoryDto(expected.get(0).getCategory());
        assertEquals(expected.get(0).getConfirmedRequests(), actual.get(0).getConfirmedRequests());
        assertEquals(expected.get(0).getCreatedOn(), LocalDateTime.parse(actual.get(0).getCreatedOn(), DATE_FORMATTER));
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getEventDate(), LocalDateTime.parse(actual.get(0).getEventDate(), DATE_FORMATTER));
        verify(userMapper).mapToUserShortDto(expected.get(0).getInitiator());
        verify(locationMapper).mapToLocationDto(expected.get(0).getLocation());
        assertEquals(expected.get(0).getPaid(), actual.get(0).isPaid());
        assertEquals(expected.get(0).getParticipantLimit(), actual.get(0).getParticipantLimit());
        assertEquals(expected.get(0).getPublishedOn(), LocalDateTime.parse(actual.get(0).getPublishedOn(), DATE_FORMATTER));
        assertEquals(expected.get(0).getRequestModeration(), actual.get(0).isRequestModeration());
        assertEquals(expected.get(0).getState().name(), actual.get(0).getState());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getViews(), actual.get(0).getViews());
    }

    @Test
    void mapToEventShortDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToEventShortDto((Event) null);
        assertNull(actual);
    }

    @Test
    void mapToEventShortDto() {
        Event expected = getEvent();

        var actual = mapper.mapToEventShortDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAnnotation(), actual.getAnnotation());
        verify(categoryMapper).mapToCategoryDto(expected.getCategory());
        assertEquals(expected.getConfirmedRequests(), actual.getConfirmedRequests());
        assertEquals(expected.getEventDate(), LocalDateTime.parse(actual.getEventDate(), DATE_FORMATTER));
        verify(userMapper).mapToUserShortDto(expected.getInitiator());
        assertEquals(expected.getPaid(), actual.isPaid());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getViews(), actual.getViews());
    }

    @Test
    void mapToEventShortDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapToEventShortDto((List<Event>) null);
        assertNull(actual);
    }

    @Test
    void mapToEventShortDto_ifSrcList() {
        List<Event> expected = List.of(getEvent());

        var actual = mapper.mapToEventShortDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getAnnotation(), actual.get(0).getAnnotation());
        verify(categoryMapper).mapToCategoryDto(expected.get(0).getCategory());
        assertEquals(expected.get(0).getConfirmedRequests(), actual.get(0).getConfirmedRequests());
        assertEquals(expected.get(0).getEventDate(), LocalDateTime.parse(actual.get(0).getEventDate(), DATE_FORMATTER));
        verify(userMapper).mapToUserShortDto(expected.get(0).getInitiator());
        assertEquals(expected.get(0).getPaid(), actual.get(0).isPaid());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getViews(), actual.get(0).getViews());
    }

    @Test
    void updateEvent_ifSrcAndUpdateEventUserRequestNull_thenTargetNull() {
        var actual = mapper.updateEvent(null, (UpdateEventUserRequest) null);
        assertNull(actual);
    }

    @Test
    void updateEvent_ifSrcAndUpdateEventUserRequest() {
        Event expectedEvent = getEvent();
        var expectedRequest = UpdateEventUserRequest
                .builder()
                .annotation("updAnno")
                .description("updDescr")
                .eventDate(LocalDateTime.now())
                .location(LocationDto.builder().build())
                .paid(!expectedEvent.getPaid())
                .participantLimit(expectedEvent.getParticipantLimit() + 100)
                .requestModeration(!expectedEvent.getRequestModeration())
                .stateAction(StateAction.REJECT_EVENT)
                .title("updTitle")
                .build();

        var actual = mapper.updateEvent(expectedEvent, expectedRequest);

        assertNotNull(actual);
        assertEquals(expectedRequest.getTitle(), actual.getTitle());
        assertEquals(expectedRequest.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedRequest.getDescription(), actual.getDescription());
        assertEquals(expectedRequest.getEventDate(), actual.getEventDate());
        verify(locationMapper).mapToLocation(expectedRequest.getLocation());
        assertEquals(expectedRequest.getPaid(), actual.getPaid());
        assertEquals(expectedRequest.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedRequest.getRequestModeration(), actual.getRequestModeration());
        assertEquals(EventState.CANCELED, actual.getState());

        assertEquals(expectedEvent.getId(), actual.getId());
        assertEquals(expectedEvent.getCategory(), actual.getCategory());
        assertEquals(expectedEvent.getInitiator(), actual.getInitiator());
        assertEquals(expectedEvent.getPublishedOn(), actual.getPublishedOn());
    }

    @Test
    void updateEvent_ifSrcNullAndUpdateEventUserRequest_thenNewEventByRequest() {
        var expectedRequest = UpdateEventUserRequest
                .builder()
                .annotation("updAnno")
                .description("updDescr")
                .eventDate(LocalDateTime.now())
                .location(LocationDto.builder().build())
                .paid(true)
                .participantLimit(10)
                .requestModeration(true)
                .stateAction(StateAction.REJECT_EVENT)
                .title("updTitle")
                .build();

        var actual = mapper.updateEvent(null, expectedRequest);

        assertNotNull(actual);
        assertEquals(expectedRequest.getTitle(), actual.getTitle());
        assertEquals(expectedRequest.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedRequest.getDescription(), actual.getDescription());
        assertEquals(expectedRequest.getEventDate(), actual.getEventDate());
        verify(locationMapper).mapToLocation(expectedRequest.getLocation());
        assertEquals(expectedRequest.getPaid(), actual.getPaid());
        assertEquals(expectedRequest.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedRequest.getRequestModeration(), actual.getRequestModeration());
        assertEquals(EventState.CANCELED, actual.getState());
    }

    @Test
    void updateEvent_ifSrcAndUpdateEventUserRequestNull_thenCopySrc() {
        Event expectedEvent = getEvent();

        var actual = mapper.updateEvent(expectedEvent, (UpdateEventUserRequest) null);

        assertNotNull(actual);
        assertEquals(expectedEvent.getTitle(), actual.getTitle());
        assertEquals(expectedEvent.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedEvent.getDescription(), actual.getDescription());
        assertEquals(expectedEvent.getEventDate(), actual.getEventDate());
        assertEquals(expectedEvent.getLocation(), actual.getLocation());
        assertEquals(expectedEvent.getPaid(), actual.getPaid());
        assertEquals(expectedEvent.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedEvent.getRequestModeration(), actual.getRequestModeration());
        assertEquals(expectedEvent.getState(), actual.getState());

        assertEquals(expectedEvent.getId(), actual.getId());
        assertEquals(expectedEvent.getCategory(), actual.getCategory());
        assertEquals(expectedEvent.getInitiator(), actual.getInitiator());
        assertEquals(expectedEvent.getPublishedOn(), actual.getPublishedOn());
    }


    @Test
    void updateEvent_ifSrcAndUpdateEventAdminRequestNull_thenTargetNull() {
        var actual = mapper.updateEvent(null, (UpdateEventAdminRequest) null);
        assertNull(actual);
    }

    @Test
    void updateEvent_ifSrcAndUpdateEventAdminRequest() {
        Event expectedEvent = getEvent();
        var expectedRequest = UpdateEventAdminRequest
                .builder()
                .category(5L)
                .annotation("updAnno")
                .description("updDescr")
                .eventDate(LocalDateTime.now())
                .location(LocationDto.builder().build())
                .paid(!expectedEvent.getPaid())
                .participantLimit(expectedEvent.getParticipantLimit() + 100)
                .requestModeration(!expectedEvent.getRequestModeration())
                .stateAction(StateAction.REJECT_EVENT)
                .title("updTitle")
                .build();

        var actual = mapper.updateEvent(expectedEvent, expectedRequest);

        assertNotNull(actual);
        assertEquals(expectedRequest.getTitle(), actual.getTitle());
        assertEquals(expectedRequest.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedRequest.getDescription(), actual.getDescription());
        assertEquals(expectedRequest.getEventDate(), actual.getEventDate());
        verify(locationMapper).mapToLocation(expectedRequest.getLocation());
        assertEquals(expectedRequest.getPaid(), actual.getPaid());
        assertEquals(expectedRequest.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedRequest.getRequestModeration(), actual.getRequestModeration());
        assertEquals(EventState.CANCELED, actual.getState());
        assertNotNull(actual.getCategory());
        assertEquals(expectedRequest.getCategory(), actual.getCategory().getId());

        assertEquals(expectedEvent.getId(), actual.getId());
        assertEquals(expectedEvent.getInitiator(), actual.getInitiator());
        assertEquals(expectedEvent.getPublishedOn(), actual.getPublishedOn());
    }

    @Test
    void updateEvent_ifSrcNullAndUpdateEventAdminRequest_thenNewEventByRequest() {
        var expectedRequest = UpdateEventAdminRequest
                .builder()
                .category(5L)
                .annotation("updAnno")
                .description("updDescr")
                .eventDate(LocalDateTime.now())
                .location(LocationDto.builder().build())
                .paid(true)
                .participantLimit(100)
                .requestModeration(true)
                .stateAction(StateAction.REJECT_EVENT)
                .title("updTitle")
                .build();

        var actual = mapper.updateEvent(null, expectedRequest);

        assertNotNull(actual);
        assertEquals(expectedRequest.getTitle(), actual.getTitle());
        assertEquals(expectedRequest.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedRequest.getDescription(), actual.getDescription());
        assertEquals(expectedRequest.getEventDate(), actual.getEventDate());
        verify(locationMapper).mapToLocation(expectedRequest.getLocation());
        assertEquals(expectedRequest.getPaid(), actual.getPaid());
        assertEquals(expectedRequest.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedRequest.getRequestModeration(), actual.getRequestModeration());
        assertEquals(EventState.CANCELED, actual.getState());
        assertNotNull(actual.getCategory());
        assertEquals(expectedRequest.getCategory(), actual.getCategory().getId());
    }

    @Test
    void updateEvent_ifSrcAndUpdateEventAdminRequestNull_thenCopySrc() {
        Event expectedEvent = getEvent();

        var actual = mapper.updateEvent(expectedEvent, (UpdateEventAdminRequest) null);

        assertNotNull(actual);
        assertEquals(expectedEvent.getTitle(), actual.getTitle());
        assertEquals(expectedEvent.getAnnotation(), actual.getAnnotation());
        assertEquals(expectedEvent.getDescription(), actual.getDescription());
        assertEquals(expectedEvent.getEventDate(), actual.getEventDate());
        assertEquals(expectedEvent.getLocation(), actual.getLocation());
        assertEquals(expectedEvent.getPaid(), actual.getPaid());
        assertEquals(expectedEvent.getParticipantLimit(), actual.getParticipantLimit());
        assertEquals(expectedEvent.getRequestModeration(), actual.getRequestModeration());
        assertEquals(expectedEvent.getState(), actual.getState());

        assertEquals(expectedEvent.getId(), actual.getId());
        assertEquals(expectedEvent.getCategory(), actual.getCategory());
        assertEquals(expectedEvent.getInitiator(), actual.getInitiator());
        assertEquals(expectedEvent.getPublishedOn(), actual.getPublishedOn());
    }

    @Test
    void toEventState_ifSrcNull_thenTargetNull() {
        var actual = mapper.toEventState((String) null);
        assertNull(actual);
    }

    @Test
    void toEventState() {
        var expected = EventState.PENDING;

        var actual = mapper.toEventState(expected.name().toLowerCase());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }


    @Test
    void toEventState_ifSrcListNull_thenTargetNull() {
        var actual = mapper.toEventState((List<String>) null);
        assertNull(actual);
    }

    @Test
    void toEventState_ifSrcList() {
        var expected = List.of(EventState.PENDING.name());

        var actual = mapper.toEventState(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0), actual.get(0).name());
    }

    @Test
    void toEventSort_ifSrcListNull_thenTargetNull() {
        var actual = mapper.toEventSort((List<String>) null);
        assertNull(actual);
    }

    @Test
    void toEventSort_ifSrcList() {
        var expected = List.of(EventSort.VIEWS.name());

        var actual = mapper.toEventSort(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0), actual.get(0).name());
    }

    @Test
    void mapToEvent_ifSrcLongNull_thenTargetNull() {
        var actual = mapper.mapToEvent((Long) null);

        assertNull(actual);
    }

    @Test
    void mapToEvent_ifSrcLong() {
        Long expected = 1L;

        var actual = mapper.mapToEvent(expected);

        assertNotNull(actual);
        assertEquals(expected, actual.getId());
        assertNull(actual.getCategory());
        assertNull(actual.getTitle());
        assertNull(actual.getAnnotation());
        assertNull(actual.getDescription());
        assertNull(actual.getEventDate());
        assertNull(actual.getPaid());
        assertNull(actual.getParticipantLimit());
        assertNull(actual.getRequestModeration());
    }

    @Test
    void mapToEvent_ifSrcSetLongNull_thenTargetNull() {
        var actual = mapper.mapToEvent((Set<Long>) null);

        assertNull(actual);
    }

    @Test
    void mapToEvent_ifSrcSetLong() {
        Set<Long> expected = Set.of(1L);

        var actual = mapper
                .mapToEvent(expected)
                .stream()
                .findAny()
                .orElse(null);

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertNull(actual.getCategory());
        assertNull(actual.getTitle());
        assertNull(actual.getAnnotation());
        assertNull(actual.getDescription());
        assertNull(actual.getEventDate());
        assertNull(actual.getPaid());
        assertNull(actual.getParticipantLimit());
        assertNull(actual.getRequestModeration());
    }
}