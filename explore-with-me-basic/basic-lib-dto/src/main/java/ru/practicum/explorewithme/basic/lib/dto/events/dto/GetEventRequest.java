package ru.practicum.explorewithme.basic.lib.dto.events.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventSort;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class GetEventRequest {
    private final List<Long> users;
    private final List<EventState> states;
    private final List<Long> categories;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;

    private final String text;
    private final Boolean paid;
    private final Boolean onlyAvailable;
    private final EventSort sort;

    private final Long placeId;
}
