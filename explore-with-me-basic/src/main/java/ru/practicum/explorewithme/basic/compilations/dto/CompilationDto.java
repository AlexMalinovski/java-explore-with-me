package ru.practicum.explorewithme.basic.compilations.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.events.dto.EventShortDto;

import java.util.List;

@Builder
@Getter
public class CompilationDto {
    private final Long id;
    private final List<EventShortDto> events;
    private final Boolean pinned;
    private final String title;
}
