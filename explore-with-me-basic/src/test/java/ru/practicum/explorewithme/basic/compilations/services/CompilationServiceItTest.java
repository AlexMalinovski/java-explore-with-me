package ru.practicum.explorewithme.basic.compilations.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.events.models.Event;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CompilationServiceItTest {

    @Autowired
    private CompilationService compilationService;

    @Test
    @Sql("/compilation-service-it-test.sql")
    void crud() {
        Compilation expected = Compilation.builder()
                .title("unique title")
                .pinned(true)
                .events(Set.of(Event.builder().id(1L).build()))
                .build();

        Compilation created = compilationService.createCompilation(expected);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(expected.getTitle(), created.getTitle());
        assertEquals(expected.getPinned(), created.getPinned());
        assertEquals(expected.getEvents().size(), created.getEvents().size());

        UpdateCompilationRequest request = UpdateCompilationRequest.builder().title("new title").build();

        Compilation updated = compilationService.updateCompilation(created.getId(), request);

        assertNotNull(updated);
        assertEquals(request.getTitle(), updated.getTitle());
        assertEquals(created.getPinned(), updated.getPinned());
        assertEquals(created.getEvents().size(), updated.getEvents().size());

        compilationService.deleteCompilation(created.getId());

        assertThrows(NotFoundException.class, () -> compilationService.getCompilationById(created.getId()));
    }
}