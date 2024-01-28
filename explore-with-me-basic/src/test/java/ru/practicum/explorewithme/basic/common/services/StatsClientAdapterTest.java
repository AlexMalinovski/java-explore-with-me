package ru.practicum.explorewithme.basic.common.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.explorewithme.statistics.client.StatsClient;
import ru.practicum.explorewithme.statistics.lib.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsClientAdapterTest {

    @Mock
    private StatsClient statsClient;

    @InjectMocks
    private StatsClientAdapter statsClientAdapter;

    @Test
    void requestHitsForEvents_ifStatsClientErrorResponse_thenOptionalEmpty() {
        ResponseEntity<List<ViewStatsDto>> statsResult = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(statsClient.getStats(any(LocalDateTime.class), any(LocalDateTime.class), anySet(), anyBoolean()))
                .thenReturn(statsResult);

        Optional<Map<Long, Long>> actual = statsClientAdapter.requestHitsForEvents(List.of(1L));

        assertNotNull(actual);
        verify(statsClient).getStats(any(LocalDateTime.class), any(LocalDateTime.class), anySet(), anyBoolean());
        assertTrue(actual.isEmpty());
    }

    @Test
    void requestHitsForEvents_returnMapOfHitsById() {
        Long expectedEventId = 1L;
        Long expectedEventHits = 10L;
        String expectedEventUri = "/events/1";

        ViewStatsDto view = ViewStatsDto.builder()
                .app("app")
                .uri(expectedEventUri)
                .hits(expectedEventHits)
                .build();
        ResponseEntity<List<ViewStatsDto>> statsResult = ResponseEntity.ok(List.of(view));
        ArgumentCaptor<Set<String>> urisCaptor = ArgumentCaptor.forClass(Set.class);
        when(statsClient.getStats(any(LocalDateTime.class), any(LocalDateTime.class), anySet(), anyBoolean()))
                .thenReturn(statsResult);

        Optional<Map<Long, Long>> actual = statsClientAdapter.requestHitsForEvents(List.of(expectedEventId));

        assertNotNull(actual);

        verify(statsClient).getStats(any(LocalDateTime.class), any(LocalDateTime.class), urisCaptor.capture(), anyBoolean());
        Set<String> argUris = urisCaptor.getValue();
        assertTrue(argUris.contains(expectedEventUri));

        assertFalse(actual.isEmpty());
        Map<Long, Long> actualMap = actual.get();
        assertTrue(actualMap.containsKey(expectedEventId));
        assertEquals(expectedEventHits, actualMap.get(expectedEventId));
    }
}