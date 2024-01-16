package ru.practicum.explorewithme.statistics.service.visits.mappers;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.statistics.lib.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    default ViewStatsDto mapToViewStatsDto(ViewStats viewStats) {
        if (viewStats == null) {
            return null;
        }
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .hits(viewStats.getHits())
                .uri(viewStats.getUri())
                .build();
    }

    List<ViewStatsDto> mapToViewStatsDto(List<ViewStats> viewStats);
}
