package ru.practicum.explorewithme.basic.common.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.events.enums.EventState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumMapperTest {

    private final EnumMapper mapper = new EnumMapper();

    @Test
    void toEnum() {
        EventState expected = EventState.PENDING;

        var actual = mapper.toEnum(expected.name(), EventState.class);

        assertNotNull(actual);
        assertEquals(expected, actual);

        actual = mapper.toEnum(expected.name().toLowerCase(), EventState.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void toEnum_ifSrcNull_thenTargetNull() {
        var actual = mapper.toEnum(null, EventState.class);
        assertNull(actual);
    }

    @Test
    void toEnum_ifSrcInvalid_thenThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> mapper.toEnum("jsbdhj", EventState.class));
    }
}