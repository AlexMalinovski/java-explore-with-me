package ru.practicum.explorewithme.basic.service.events.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.NewEventDto;

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