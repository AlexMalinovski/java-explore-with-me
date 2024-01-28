package ru.practicum.explorewithme.basic.events.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NewEventDtoTest {

    @Test
    void builder_defaultValues() {
        NewEventDto actual = NewEventDto.builder().build();

        assertFalse(actual.isPaid());
        assertEquals(0, actual.getParticipantLimit());
        assertTrue(actual.isRequestModeration());
    }
}