package ru.practicum.explorewithme.basic.lib.dto.events.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.users.UserShortDto;

@Builder
@Getter
public class EventFullDto {
    private final long id;
    private final String annotation;
    private final CategoryDto category;
    private final long confirmedRequests;
    private final String createdOn; //"yyyy-MM-dd HH:mm:ss"
    private final String description;
    private final String eventDate; // "yyyy-MM-dd HH:mm:ss"
    private final UserShortDto initiator;
    private final LocationDto location;
    private final boolean paid;
    private final int participantLimit;
    private final String publishedOn; //"yyyy-MM-dd HH:mm:ss"
    private final boolean requestModeration;
    private final String state; //enum state -> name
    private final String title;
    private final long views;
}
