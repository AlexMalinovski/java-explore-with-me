package ru.practicum.explorewithme.statistics.lib.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}
