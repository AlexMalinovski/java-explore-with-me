package ru.practicum.explorewithme.basic.compilations.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.compilations.repositories.CompilationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompilationServiceImplTest {

    @Mock
    private CompilationRepository compilationRepository;

    @Mock
    private CompilationMapper compilationMapper;

    @InjectMocks
    private CompilationServiceImpl compilationService;

    @Test
    void createCompilation_ifWithId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> compilationService.createCompilation(Compilation.builder().id(1L).build()));

        verify(compilationRepository, never()).save(any());
    }

    @Test
    void createCompilation() {
        Compilation expected = Compilation.builder().build();
        when(compilationRepository.save(any())).thenReturn(expected);

        var actual = compilationService.createCompilation(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(compilationRepository).save(expected);
    }

    @Test
    void deleteCompilation_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> compilationService.deleteCompilation(-1L));
        assertThrows(NotFoundException.class, () -> compilationService.deleteCompilation(0L));

        verify(compilationRepository, never()).deleteById(any());
    }

    @Test
    void deleteCompilation() {
        long expected = 1L;

        compilationService.deleteCompilation(expected);

        verify(compilationRepository).deleteById(expected);
    }

    @Test
    void updateCompilation_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> compilationService.updateCompilation(-1L, UpdateCompilationRequest.builder().build()));
        assertThrows(NotFoundException.class, () -> compilationService.updateCompilation(0L, UpdateCompilationRequest.builder().build()));

        verify(compilationRepository, never()).save(any());
    }

    @Test
    void updateCompilation_ifNotFound_thenThrowNotFoundException() {
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> compilationService.updateCompilation(1L, UpdateCompilationRequest.builder().build()));

        verify(compilationRepository, never()).save(any());
    }

    @Test
    void updateCompilation() {
        UpdateCompilationRequest request = UpdateCompilationRequest.builder().build();
        Compilation expected = Compilation.builder().build();
        when(compilationRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(compilationMapper.updateCompilation(any(Compilation.class), any(UpdateCompilationRequest.class))).thenReturn(expected);
        when(compilationRepository.findCompilationById(anyLong())).thenReturn(Optional.of(expected));

        var actual = compilationService.updateCompilation(1L, request);

        verify(compilationRepository).findById(1L);
        verify(compilationMapper).updateCompilation(expected, request);
        verify(compilationRepository).save(expected);
        verify(compilationRepository).findCompilationById(1L);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getCompilationsPublic() {
        List<Compilation> expected = List.of(Compilation.builder().build());
        when(compilationRepository.findByWithOffsetAndLimitFetch(any(BooleanExpression.class), anyInt(), anyInt())).thenReturn(expected);

        var actual = compilationService.getCompilationsPublic(false, 0, 10);

        verify(compilationRepository).findByWithOffsetAndLimitFetch(any(BooleanExpression.class), eq(0), eq(10));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getCompilationById_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> compilationService.getCompilationById(-1L));
        assertThrows(NotFoundException.class, () -> compilationService.getCompilationById(0L));

        verify(compilationRepository, never()).findCompilationById(anyLong());
    }

    @Test
    void getCompilationById_ifNotFound_thenThrowNotFoundException() {
        when(compilationRepository.findCompilationById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> compilationService.getCompilationById(1L));

        verify(compilationRepository).findCompilationById(1L);
    }

    @Test
    void getCompilationById() {
        Compilation expected = Compilation.builder().build();
        when(compilationRepository.findCompilationById(anyLong())).thenReturn(Optional.of(expected));

        var actual = compilationService.getCompilationById(1L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(compilationRepository).findCompilationById(1L);
    }
}