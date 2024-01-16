package ru.practicum.explorewithme.statistics.service.visits.services;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;
import ru.practicum.explorewithme.statistics.service.visits.repositories.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    @NonNull
    public EndpointHit createEndpointHit(@NonNull EndpointHit hit) {
        return endpointHitRepository.save(hit);
    }

    @Override
    @NonNull
    public List<ViewStats> getViewStats(@NonNull LocalDateTime start, @NonNull LocalDateTime end,
                                        @Nullable Collection<String> uris, boolean unique) {
        if (unique) {
            return endpointHitRepository.countUniqueHitsByAppAndUriForPeriod(start, end, uris);
        }
        return endpointHitRepository.countHitsByAppAndUriForPeriod(start, end, uris);
    }
}
