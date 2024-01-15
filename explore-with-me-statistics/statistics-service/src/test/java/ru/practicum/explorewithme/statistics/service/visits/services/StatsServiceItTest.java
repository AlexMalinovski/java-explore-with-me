package ru.practicum.explorewithme.statistics.service.visits.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StatsServiceItTest {

    @Autowired
    StatsService statsService;

    @Test
    @Sql({"/endpoint-hit-repository-it-test.sql"})
    void getViewStats() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 12, 31, 0, 0);

        List<ViewStats> actual = statsService.getViewStats(start, end, List.of("uri1", "uri2"), true);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("app", actual.get(0).getApp());
        assertEquals("uri1", actual.get(0).getUri());
        assertEquals(1, actual.get(0).getHits());
    }
}