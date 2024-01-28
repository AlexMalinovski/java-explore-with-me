package ru.practicum.explorewithme.basic.compilations.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewCompilationDtoTest {

    @Test
    void createdWithDefaultValues() {
        NewCompilationDto actual = NewCompilationDto.builder().build();

        assertNotNull(actual);
        assertFalse(actual.isPinned());
    }
}