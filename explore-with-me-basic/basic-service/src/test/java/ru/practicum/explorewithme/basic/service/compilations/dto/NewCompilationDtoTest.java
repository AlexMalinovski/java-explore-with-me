package ru.practicum.explorewithme.basic.service.compilations.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.lib.dto.compilations.NewCompilationDto;

import static org.junit.jupiter.api.Assertions.*;

class NewCompilationDtoTest {

    @Test
    void createdWithDefaultValues() {
        NewCompilationDto actual = NewCompilationDto.builder().build();

        assertNotNull(actual);
        assertFalse(actual.isPinned());
    }
}