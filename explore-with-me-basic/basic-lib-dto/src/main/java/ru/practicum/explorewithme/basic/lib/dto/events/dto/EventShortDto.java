package ru.practicum.explorewithme.basic.lib.dto.events.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.lib.dto.categories.CategoryDto;
import ru.practicum.explorewithme.basic.lib.dto.users.UserShortDto;

@Builder
@Getter
public class EventShortDto {
    private final long id;
    private final String annotation;
    private final CategoryDto category;
    private final long confirmedRequests;
    private final String eventDate; // "yyyy-MM-dd HH:mm:ss"
    private final UserShortDto initiator;
    private final boolean paid;
    private final String title;
    private final long views;
}
