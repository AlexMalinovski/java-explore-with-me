package ru.practicum.explorewithme.statistics.service.visits.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;
import ru.practicum.explorewithme.statistics.service.visits.repositories.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private EndpointHitRepository repository;

    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void createEndpointHit_invokeSaveAndReturnSaved() {
        EndpointHit expected = EndpointHit.builder().build();
        when(repository.save(any())).thenReturn(expected);

        EndpointHit actual = statsService.createEndpointHit(expected);

        verify(repository).save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getViewStats_ifUnique_thenReturnUnique() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        List<String> uris = List.of("uri1");
        List<ViewStats> expected = new ArrayList<>();
        when(repository.countUniqueHitsByAppAndUriForPeriod(any(LocalDateTime.class), any(LocalDateTime.class), anyCollection()))
                .thenReturn(expected);

        List<ViewStats> actual = statsService.getViewStats(start, end, uris, true);

        verify(repository).countUniqueHitsByAppAndUriForPeriod(start, end, uris);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getViewStats_ifNotUnique_thenReturnNotUnique() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        List<String> uris = List.of("uri1");
        List<ViewStats> expected = new ArrayList<>();
        when(repository.countHitsByAppAndUriForPeriod(any(LocalDateTime.class), any(LocalDateTime.class), anyCollection()))
                .thenReturn(expected);

        List<ViewStats> actual = statsService.getViewStats(start, end, uris, false);

        verify(repository).countHitsByAppAndUriForPeriod(start, end, uris);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}