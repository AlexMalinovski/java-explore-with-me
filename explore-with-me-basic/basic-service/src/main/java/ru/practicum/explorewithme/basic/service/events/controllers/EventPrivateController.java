package ru.practicum.explorewithme.basic.service.events.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventShortDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.StateAction;
import ru.practicum.explorewithme.basic.service.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.service.events.models.Event;
import ru.practicum.explorewithme.basic.service.events.services.EventService;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.service.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;
import ru.practicum.explorewithme.basic.service.users.models.User;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
@Validated
public class EventPrivateController {

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ParticipationMapper participationMapper;

    /**
     * Добавление нового события.
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.
     * Эндпоинт: POST /users/{userId}/events
     *
     * @param userId id текущего пользователя
     * @param dto    NewEventDto
     * @return EventFullDto
     */
    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto dto) {
        Event event = eventMapper.mapToEvent(dto)
                .toBuilder()
                .initiator(User.builder()
                        .id(userId)
                        .build())
                .build();
        LocalDateTime now = LocalDateTime.now();
        if (event.getEventDate().minusHours(2).isBefore(now)) {
            throw new BadRequestException(
                    String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s", dto.getEventDate()));
        }
        Event created = eventService.createEvent(event);

        return new ResponseEntity<>(eventMapper.mapToEventFullDto(created), HttpStatus.CREATED);
    }

    /**
     * Получение событий, добавленных текущим пользователем.
     * Эндпоинт: GET /users/{userId}/events
     *
     * @param userId id текущего пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return List EventShortDto
     */
    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable long userId,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        List<Event> events = eventService.getEvents(userId, from, size);

        return ResponseEntity.ok(eventMapper.mapToEventShortDto(events));
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * Эндпоинт: GET /users/{userId}/events/{eventId}
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return EventFullDto
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable long userId, @PathVariable long eventId) {
        Event event = eventService.getEventById(userId, eventId);
        return ResponseEntity.ok(eventMapper.mapToEventFullDto(event));
    }

    /**
     * Изменение события добавленного текущим пользователем.
     * Эндпоинт: PATH /users/{userId}/events/{eventId}
     *
     * @param userId  id текущего пользователя
     * @param eventId id редактируемого события
     * @param request UpdateEventUserRequest
     * @return EventFullDto
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable long userId, @PathVariable long eventId,
                                                    @RequestBody @Valid UpdateEventUserRequest request) {

        Optional.ofNullable(request.getStateAction())
                .ifPresent(action -> {
                    if (!(action == StateAction.SEND_TO_REVIEW || action == StateAction.CANCEL_REVIEW)) {
                        throw new BadRequestException("Invalid action");
                    }
                });
        Optional.ofNullable(request.getEventDate())
                .ifPresent(date -> {
                    if (date.minusHours(2).isBefore(LocalDateTime.now())) {
                        throw new BadRequestException(
                                String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                                        date.format(DateTimeFormatter.ISO_DATE_TIME)));
                    }
                });
        Event event = eventService.updateEvent(userId, eventId, request);

        return ResponseEntity.ok(eventMapper.mapToEventFullDto(event));
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя.
     * Эндпоинт: GET /users/{userId}/events/{eventId}/requests
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return List ParticipationRequestDto
     */
    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipation(
            @PathVariable long userId, @PathVariable long eventId) {

        List<Participation> participation = eventService.getEventParticipation(userId, eventId);
        return ResponseEntity.ok(participationMapper.toParticipationRequestDto(participation));
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.
     * Эндпоинт: PATCH /users/{userId}/events/{eventId}/requests
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @param request EventRequestStatusUpdateRequest
     * @return EventRequestStatusUpdateResult
     */
    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventState(
            @PathVariable long userId, @PathVariable long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request) {

        Optional.ofNullable(request.getStatus())
                .ifPresent(status -> {
                    if (!(status == ParticipationState.CONFIRMED || status == ParticipationState.REJECTED)) {
                        throw new BadRequestException("Invalid status");
                    }
                });
        EventRequestStatusUpdateResult result = eventService.updateEventState(userId, eventId, request);

        return ResponseEntity.ok(result);
    }
}
