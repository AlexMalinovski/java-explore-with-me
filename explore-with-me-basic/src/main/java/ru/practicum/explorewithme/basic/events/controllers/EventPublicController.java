package ru.practicum.explorewithme.basic.events.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
@Validated
public class EventPublicController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable long eventId) {
        Event event = eventService.getPublishEventById(eventId);
        return ResponseEntity.ok(eventMapper.mapToEventFullDto(event));
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        if (rangeStart != null && rangeEnd != null
                && (rangeStart.equals(rangeEnd) || rangeEnd.isBefore(rangeStart))) {
            throw new BadRequestException("rangeEnd is equals or before rangeStart");
        }
        GetEventRequest getEventRequest = GetEventRequest.builder()
                .text(text != null ? text.toLowerCase(Locale.ROOT) : null)
                .paid(paid)
                .onlyAvailable(onlyAvailable)
                .sort(eventMapper.toEventSort(sort))
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        List<Event> events = eventService.getEventsPublic(getEventRequest, from, size);
        return ResponseEntity.ok(eventMapper.mapToEventFullDto(events));
    }
}
