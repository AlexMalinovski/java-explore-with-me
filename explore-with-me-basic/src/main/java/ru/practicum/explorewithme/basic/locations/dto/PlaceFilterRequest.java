package ru.practicum.explorewithme.basic.locations.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.locations.enums.PlaceSort;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;

import java.util.Set;

@Builder
@Getter
public class PlaceFilterRequest {
    private final Set<PlaceStatus> statuses;
    private final String name;
    private final String description;

    private final CircleSearchArea area;

    @Builder.Default
    private final PlaceSort orderBy = PlaceSort.DEFAULT;
}
