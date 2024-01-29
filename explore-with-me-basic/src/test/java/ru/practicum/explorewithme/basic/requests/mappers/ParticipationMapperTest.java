package ru.practicum.explorewithme.basic.requests.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.requests.models.Participation;
import ru.practicum.explorewithme.basic.users.models.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ParticipationMapperTest {

    private final ParticipationMapper mapper = new ParticipationMapperImpl();

    @Test
    void toParticipationRequestDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.toParticipationRequestDto((Participation) null);

        assertNull(actual);
    }

    @Test
    void toParticipationRequestDto() {
        Participation expected = Participation.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .event(Event.builder().id(2L).build())
                .requester(User.builder().id(3L).build())
                .status(ParticipationState.CONFIRMED)
                .build();

        var actual = mapper.toParticipationRequestDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCreated(), actual.getCreated());
        assertEquals(expected.getEvent().getId(), actual.getEvent());
        assertEquals(expected.getRequester().getId(), actual.getRequester());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    void toParticipationRequestDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.toParticipationRequestDto((List<Participation>) null);

        assertNull(actual);
    }

    @Test
    void testToParticipationRequestDto_ifSrcList() {
        List<Participation> expected = List.of(Participation.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .event(Event.builder().id(2L).build())
                .requester(User.builder().id(3L).build())
                .status(ParticipationState.CONFIRMED)
                .build());

        var actual = mapper.toParticipationRequestDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getCreated(), actual.get(0).getCreated());
        assertEquals(expected.get(0).getEvent().getId(), actual.get(0).getEvent());
        assertEquals(expected.get(0).getRequester().getId(), actual.get(0).getRequester());
        assertEquals(expected.get(0).getStatus(), actual.get(0).getStatus());
    }

    @Test
    void toEventState() {
        for (ParticipationState state : ParticipationState.values()) {
            var actual = mapper.toEventState(state.name());

            assertNotNull(actual);
            assertEquals(state, actual);

            actual = mapper.toEventState(state.name().toLowerCase());

            assertNotNull(actual);
            assertEquals(state, actual);
        }
    }
}