package ru.practicum.explorewithme.basic.common.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.statistics.client.StatsClient;
import ru.practicum.explorewithme.statistics.lib.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatsClientAdapter {

    private final Pattern eventPathPattern = Pattern.compile("^/events/(?<id>\\d+)(\\?.+)?$");
    private final LocalDateTime defaultDateFrom = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
    private final StatsClient statsClient;

    @RequiredArgsConstructor
    @Getter
    private static final class ParseResult {
        private final Long id;
        private final Long views;
    }

    private final BiFunction<ViewStatsDto, Pattern, Optional<ParseResult>> parseViewStatsDto = (dto, regEx) -> {
        Matcher matcher = regEx.matcher(dto.getUri());
        if (matcher.matches()) {
            try {
                Long id = Long.valueOf(matcher.group("id"));
                return Optional.of(new ParseResult(id, dto.getHits()));
            } catch (NumberFormatException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    };

    public Optional<Map<Long, Long>> requestHitsForEvents(final List<Long> eventIds) {
        //Map<id, views>
        Set<String> uris = eventIds.stream()
                .map(id -> String.format("/events/%d", id))
                .collect(Collectors.toSet());

        ResponseEntity<List<ViewStatsDto>> stats = statsClient.getStats(
                defaultDateFrom,
                LocalDateTime.now(),
                uris, true);
        if (!stats.getStatusCode().is2xxSuccessful() || stats.getBody() == null) {
            return Optional.empty();
        }

        Map<Long, Long> collect = stats.getBody().stream()
                .filter(Objects::nonNull)
                .filter(dto -> dto.getUri() != null && dto.getHits() != null)
                .map(viewStatsDto -> parseViewStatsDto.apply(viewStatsDto, eventPathPattern))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        ParseResult::getId,
                        ParseResult::getViews,
                        Long::sum));

        return Optional.of(collect);
    }
}
