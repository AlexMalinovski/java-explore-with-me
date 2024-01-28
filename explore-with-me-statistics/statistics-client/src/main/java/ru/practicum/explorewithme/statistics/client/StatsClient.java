package ru.practicum.explorewithme.statistics.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate restTemplate;

    public ResponseEntity<EndpointHitDto> postHit(@NonNull String app, @NonNull String uri, @NonNull String ip,
                                                  @NonNull LocalDateTime timestamp) {
        CreateEndpointHitDto body = CreateEndpointHitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return makeAndSendRequest(HttpMethod.POST, "/hit", null, body, new ParameterizedTypeReference<>() {
        });
    }

    public ResponseEntity<List<ViewStatsDto>> getStats(@NonNull LocalDateTime start, @NonNull LocalDateTime end,
                                                       @Nullable Collection<String> uris, boolean unique) {
        StringBuilder sb = new StringBuilder("/stats?start={start}&end={end}&unique={unique}");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("unique", unique);
        if (uris != null) {
            sb.append("&uris={uris[]}");
            parameters.put("uris[]", uris.toArray());
        }
        return makeAndSendRequest(HttpMethod.GET, sb.toString(), parameters, null, new ParameterizedTypeReference<>() {
        });
    }

    private <T, R> ResponseEntity<R> makeAndSendRequest(
            HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body,
            ParameterizedTypeReference<R> respType) {

        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<R> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = restTemplate.exchange(path, method, requestEntity, respType, parameters);
            } else {
                serverResponse = restTemplate.exchange(path, method, requestEntity, respType);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
        return prepareClientResponse(serverResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <R> ResponseEntity<R> prepareClientResponse(ResponseEntity<R> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(null);
        }

        return responseBuilder.build();
    }
}
