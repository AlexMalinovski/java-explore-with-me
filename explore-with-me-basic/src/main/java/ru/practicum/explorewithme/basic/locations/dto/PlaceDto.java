package ru.practicum.explorewithme.basic.locations.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceDto {
    private final Long id;
    private final String name;
    private final String description;
    private final LocationDto location;
    private final Integer radius;
    private final String status;
}
