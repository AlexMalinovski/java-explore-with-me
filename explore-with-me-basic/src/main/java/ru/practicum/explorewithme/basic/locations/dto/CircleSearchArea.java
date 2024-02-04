package ru.practicum.explorewithme.basic.locations.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public final class CircleSearchArea {
    private final Double lat;
    private final Double lon;
    private final int radius;
}
