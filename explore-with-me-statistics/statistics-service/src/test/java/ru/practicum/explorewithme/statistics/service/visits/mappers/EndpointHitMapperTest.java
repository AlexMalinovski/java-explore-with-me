package ru.practicum.explorewithme.statistics.service.visits.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EndpointHitMapperTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EndpointHitMapper mapper = new EndpointHitMapperImpl();

    @Test
    void mapToEndpointHit_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToEndpointHit(null);
        assertNull(actual);
    }

    @Test
    void mapToEndpointHit() {
        CreateEndpointHitDto expected = CreateEndpointHitDto.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .timestamp("2023-01-01 00:00:00")
                .build();

        var actual = mapper.mapToEndpointHit(expected);

        assertNotNull(actual);
        assertEquals(expected.getApp(), actual.getApp());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getIp(), actual.getIp());
        assertEquals(expected.getTimestamp(), actual.getTimestamp().format(DATE_FORMATTER));
    }

    @Test
    void mapToEndpointHitDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToEndpointHitDto(null);
        assertNull(actual);
    }

    @Test
    void mapToEndpointHitDto() {
        EndpointHit expected = EndpointHit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .timestamp(LocalDateTime.of(2023, 1, 1, 0, 0, 0))
                .build();

        var actual = mapper.mapToEndpointHitDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getApp(), actual.getApp());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getIp(), actual.getIp());
        assertEquals(expected.getTimestamp(), LocalDateTime.parse(actual.getTimestamp(), DATE_FORMATTER));
    }
}