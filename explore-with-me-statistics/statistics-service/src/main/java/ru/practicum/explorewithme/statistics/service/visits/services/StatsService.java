package ru.practicum.explorewithme.statistics.service.visits.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsService {
    @NonNull
    EndpointHit createEndpointHit(@NonNull EndpointHit hit);

    @NonNull
    List<ViewStats> getViewStats(@NonNull LocalDateTime start, @NonNull LocalDateTime end,
                                 @Nullable Collection<String> uris, boolean unique);
}
