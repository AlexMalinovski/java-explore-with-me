package ru.practicum.explorewithme.basic.events.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.services.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
@Validated
public class EventAdminController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     * Эндпоинт: PATH /admin/events/{eventId}
     *
     * @param eventId id события
     * @param request UpdateEventAdminRequest
     * @return EventFullDto
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable long eventId,
                                                    @RequestBody @Valid UpdateEventAdminRequest request) {

        Optional.ofNullable(request.getStateAction())
                .ifPresent(action -> {
                    if (!(action == StateAction.PUBLISH_EVENT || action == StateAction.REJECT_EVENT)) {
                        throw new BadRequestException("Invalid action");
                    }
                });

        Event event = eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(eventMapper.mapToEventFullDto(event));
    }

    /**
     * Поиск событий.
     * Эндпоинт: GET /admin/events
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param states     список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора
     * @param size       количество событий в наборе
     * @return List EventFullDto
     */
    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEventsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        GetEventRequest getEventRequest = GetEventRequest.builder()
                .users(users)
                .states(eventMapper.toEventState(states))
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();
        List<Event> events = eventService.getEventsAdmin(getEventRequest, from, size);
        return ResponseEntity.ok(eventMapper.mapToEventFullDto(events));
    }
}
