package ru.practicum.explorewithme.statistics.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsClientTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StatsClient statsClient;

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Test
    void postHit_invokeRestTemplateExchange() {
        CreateEndpointHitDto dto = CreateEndpointHitDto.builder()
                .app("app")
                .ip("ip")
                .uri("192.192.192.192")
                .timestamp("2023-01-01 00:00:00")
                .build();
        HttpEntity<CreateEndpointHitDto> expected = new HttpEntity<>(dto, defaultHeaders());
        ArgumentCaptor<ParameterizedTypeReference> captor = ArgumentCaptor.forClass(ParameterizedTypeReference.class);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), (HttpEntity<?>) any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(EndpointHitDto.builder().build()));

        ResponseEntity<EndpointHitDto> actual = statsClient.postHit(dto.getApp(), dto.getUri(),
                dto.getIp(), LocalDateTime.from(DATE_FORMATTER.parse(dto.getTimestamp())));

        assertNotNull(actual);
        ;
        verify(restTemplate).exchange(eq("/hit"), eq(HttpMethod.POST), eq(expected), (ParameterizedTypeReference<Object>) any());
    }

    @Test
    void getStats_invokeRestTemplateExchange() {
        List<String> uris = List.of("uri/1", "uri/2");
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        String expectedPath = "/stats?start={start}&end={end}&unique={unique}&uris={uris[]}";
        HttpEntity<CreateEndpointHitDto> expectedHttpEntity = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> expectedParameters = Map.of(
                "start", start.format(DATE_FORMATTER),
                "end", end.format(DATE_FORMATTER),
                "unique", Boolean.FALSE,
                "uris[]", uris.toArray()
        );
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), (HttpEntity<?>) any(HttpEntity.class), (ParameterizedTypeReference<Object>) any(), anyMap()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(new Object()));

        statsClient.getStats(start, end, uris, false);

        verify(restTemplate).exchange(eq(expectedPath), eq(HttpMethod.GET), eq(expectedHttpEntity), (ParameterizedTypeReference<Object>) any(), captor.capture());
        Map<String, Object> actualParameters = captor.getValue();
        assertEquals(expectedParameters.size(), actualParameters.size());
        assertEquals(expectedParameters.get("start"), actualParameters.get("start"));
        assertEquals(expectedParameters.get("end"), actualParameters.get("end"));
        assertEquals(expectedParameters.get("unique"), actualParameters.get("unique"));
        assertArrayEquals((Object[]) expectedParameters.get("uris[]"), (Object[]) actualParameters.get("uris[]"));
    }
}