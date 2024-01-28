package ru.practicum.explorewithme.basic.compilations.mappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.basic.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.basic.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.models.Event;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompilationMapperTest {

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private CompilationMapperImpl mapper;

    @Test
    void toCompilation_ifSrcNull_thenTargetNull() {
        var actual = mapper.toCompilation(null);

        assertNull(actual);
    }

    @Test
    void toCompilation() {
        NewCompilationDto expected = NewCompilationDto.builder()
                .title("title")
                .events(Set.of(1L))
                .pinned(false)
                .build();

        var actual = mapper.toCompilation(expected);

        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        verify(eventMapper).mapToEvent(expected.getEvents());
        assertEquals(expected.isPinned(), actual.getPinned());
    }

    @Test
    void toCompilationDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.toCompilationDto((Compilation) null);

        assertNull(actual);
    }

    @Test
    void toCompilationDto() {
        Compilation expected = Compilation.builder()
                .title("title")
                .events(Set.of(Event.builder().build()))
                .pinned(false)
                .build();

        var actual = mapper.toCompilationDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getTitle(), actual.getTitle());
        verify(eventMapper).mapToEventShortDto(any(Event.class));
        assertEquals(expected.getPinned(), actual.getPinned());
    }

    @Test
    void toCompilationDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.toCompilationDto((List<Compilation>) null);

        assertNull(actual);
    }

    @Test
    void toCompilationDto_ifSrcList() {
        List<Compilation> expected = List.of(Compilation.builder()
                .title("title")
                .events(Set.of(Event.builder().build()))
                .pinned(false)
                .build());

        var actual = mapper.toCompilationDto(expected);

        assertNotNull(actual);
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        verify(eventMapper).mapToEventShortDto(any(Event.class));
        assertEquals(expected.get(0).getPinned(), actual.get(0).getPinned());
    }

    @Test
    void updateCompilation() {
        Compilation expectedSrc = Compilation.builder()
                .id(1L)
                .title("title")
                .pinned(true)
                .events(null)
                .build();

        UpdateCompilationRequest expectedUpd = UpdateCompilationRequest.builder()
                .title("newTitle")
                .pinned(false)
                .events(Set.of(2L))
                .build();

        var actual = mapper.updateCompilation(expectedSrc, expectedUpd);

        assertNotNull(actual);
        assertEquals(expectedSrc.getId(), actual.getId());
        assertEquals(expectedUpd.getTitle(), actual.getTitle());
        assertEquals(expectedUpd.getPinned(), actual.getPinned());
        verify(eventMapper).mapToEvent(expectedUpd.getEvents());
    }

    @Test
    void updateCompilation_ifSrcAndUpdNull_thenResultNull() {
        var actual = mapper.updateCompilation(null, null);

        assertNull(actual);
    }

    @Test
    void updateCompilation_ifUpdNull_thenResultSrc() {
        Compilation expectedSrc = Compilation.builder()
                .id(1L)
                .title("title")
                .pinned(true)
                .events(null)
                .build();

        var actual = mapper.updateCompilation(expectedSrc, null);

        assertNotNull(actual);
        assertEquals(expectedSrc.getId(), actual.getId());
        assertEquals(expectedSrc.getTitle(), actual.getTitle());
        assertEquals(expectedSrc.getPinned(), actual.getPinned());
    }
}