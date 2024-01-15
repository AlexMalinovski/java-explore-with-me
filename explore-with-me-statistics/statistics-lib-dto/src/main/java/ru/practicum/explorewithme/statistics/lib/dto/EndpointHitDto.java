package ru.practicum.explorewithme.statistics.lib.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EndpointHitDto {
    private final String app;
    private final String uri;
    private final String ip;
    private final String timestamp;
}
