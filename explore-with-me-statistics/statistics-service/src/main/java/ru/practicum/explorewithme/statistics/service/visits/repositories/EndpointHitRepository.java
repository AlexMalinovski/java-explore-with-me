package ru.practicum.explorewithme.statistics.service.visits.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select eh.app as app, eh.uri as uri, count(eh.ip) as hits " +
            "from EndpointHit as eh " +
            "where " +
            "((:uris) is null or uri in (:uris)) and " +
            "eh.timestamp >= :start and eh.timestamp <= :end " +
            "group by app, uri order by hits desc")
    List<ViewStats> countHitsByAppAndUriForPeriod(@Param("start") @NonNull LocalDateTime start,
                                                  @Param("end") @NonNull LocalDateTime end,
                                                  @Param("uris") @Nullable Collection<String> uris);

    @Query("select eh.app as app, eh.uri as uri, count(distinct eh.ip) as hits " +
            "from EndpointHit as eh " +
            "where " +
            "((:uris) is null or uri in (:uris)) and " +
            "eh.timestamp >= :start and eh.timestamp <= :end " +
            "group by app, uri order by hits desc")
    List<ViewStats> countUniqueHitsByAppAndUriForPeriod(@Param("start") @NonNull LocalDateTime start,
                                                        @Param("end") @NonNull LocalDateTime end,
                                                        @Param("uris") @Nullable Collection<String> uris);
}
