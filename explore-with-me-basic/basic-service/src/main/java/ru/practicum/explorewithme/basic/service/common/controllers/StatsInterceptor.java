package ru.practicum.explorewithme.basic.service.common.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
public class StatsInterceptor implements HandlerInterceptor {

    private final Set<Pattern> uris;

    private final StatsClient statsClient;

    private boolean isObserved(final String uri) {
        return uris.stream()
                .anyMatch(pattern -> pattern.matcher(uri).matches());
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) throws Exception {

        if (response.getStatus() >= 200 && response.getStatus() < 300) {
            String requestURI = request.getRequestURI();
            if (isObserved(requestURI)) {
                var hitResp = statsClient.postHit(
                        "ewm", requestURI, request.getRemoteAddr(), LocalDateTime.now());
                if (hitResp != null && !hitResp.getStatusCode().is2xxSuccessful()) {
                    log.error("It is impossible to save visit statistics. The statistics collection service responds with {}.",
                            hitResp.getStatusCodeValue());
                }
            }
        }
    }
}
