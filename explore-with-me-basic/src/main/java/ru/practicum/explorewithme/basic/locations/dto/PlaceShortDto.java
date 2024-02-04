package ru.practicum.explorewithme.basic.locations.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceShortDto {
    private final Long id;
    private final String name;
    private final LocationDto location;
}
