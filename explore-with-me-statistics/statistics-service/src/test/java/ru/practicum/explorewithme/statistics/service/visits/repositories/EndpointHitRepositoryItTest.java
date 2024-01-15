package ru.practicum.explorewithme.statistics.service.visits.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class EndpointHitRepositoryItTest {

    @Autowired
    private EndpointHitRepository endpointHitRepository;

    @Test
    @Sql({"/endpoint-hit-repository-it-test.sql"})
    void countHitsByAppAndUriForPeriod() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 31, 0, 0);

        List<ViewStats> viewStats = endpointHitRepository.countHitsByAppAndUriForPeriod(start, end, List.of("uri1", "uri2"));

        assertNotNull(viewStats);
        assertEquals(1, viewStats.size());
        assertEquals("app", viewStats.get(0).getApp());
        assertEquals("uri1", viewStats.get(0).getUri());
        assertEquals(3, viewStats.get(0).getHits());
    }

    @Test
    @Sql({"/endpoint-hit-repository-it-test.sql"})
    void countUniqueHitsByAppAndUriForPeriod() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 31, 0, 0);

        List<ViewStats> viewStats = endpointHitRepository.countUniqueHitsByAppAndUriForPeriod(start, end, List.of("uri1", "uri2"));

        assertNotNull(viewStats);
        assertEquals(1, viewStats.size());
        assertEquals("app", viewStats.get(0).getApp());
        assertEquals("uri1", viewStats.get(0).getUri());
        assertEquals(1, viewStats.get(0).getHits());
    }
}