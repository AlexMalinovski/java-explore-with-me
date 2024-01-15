package ru.practicum.explorewithme.statistics.service.visits.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ViewStatsMapperTest {

    private final ViewStatsMapper mapper = new ViewStatsMapperImpl();

    @Test
    void mapToViewStatsDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToViewStatsDto((ViewStats) null);
        assertNull(actual);
    }

    @Test
    void mapToViewStatsDto() {
        ViewStats expected = new ViewStats() {
            @Override
            public String getApp() {
                return "app";
            }

            @Override
            public String getUri() {
                return "uri";
            }

            @Override
            public long getHits() {
                return 10;
            }
        };

        var actual = mapper.mapToViewStatsDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getApp(), actual.getApp());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getHits(), actual.getHits());
    }

    @Test
    void mapToViewStatsDtoList_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToViewStatsDto((List<ViewStats>)  null);
        assertNull(actual);
    }

    @Test
    void mapToViewStatsDtoList() {
        List<ViewStats> expected = List.of(new ViewStats() {
            @Override
            public String getApp() {
                return "app";
            }

            @Override
            public String getUri() {
                return "uri";
            }

            @Override
            public long getHits() {
                return 10;
            }
        });

        var actual = mapper.mapToViewStatsDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getApp(), actual.get(0).getApp());
        assertEquals(expected.get(0).getUri(), actual.get(0).getUri());
        assertEquals(expected.get(0).getHits(), actual.get(0).getHits());
    }
}