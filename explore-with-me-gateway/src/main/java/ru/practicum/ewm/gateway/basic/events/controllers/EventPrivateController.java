package ru.practicum.ewm.gateway.basic.events.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventShortDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.StateAction;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/events")
@Validated
public class EventPrivateController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Добавление нового события.
     * Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.
     * Эндпоинт: POST /users/{userId}/events
     *
     * @param dto NewEventDto
     * @return EventFullDto
     */
    @PostMapping
    public Object createEvent(Principal principal, @RequestBody @Valid NewEventDto dto) {
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return basicServiceRestTemplate.postForEntity("/users/{userId}/events", dto, EventFullDto.class, userId);
    }

    /**
     * Получение событий, добавленных текущим пользователем.
     * Эндпоинт: GET /users/{userId}/events
     *
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return List EventShortDto
     */
    @GetMapping
    public Object getEvents(
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size,
            Principal principal) {

        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return basicServiceRestTemplate.getForEntity("/users/{userId}/events?from={from}&size={size}",
                EventShortDto[].class, userId, from, size);

    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * Эндпоинт: GET /users/{userId}/events/{eventId}
     *
     * @param eventId id события
     * @return EventFullDto
     */
    @GetMapping("/{eventId}")
    public Object getEventById(@PathVariable long eventId, Principal principal) {
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return basicServiceRestTemplate.getForEntity("/users/{userId}/events/{eventId}",
                EventFullDto.class, userId, eventId);
    }

    /**
     * Изменение события добавленного текущим пользователем.
     * Эндпоинт: PATH /users/{userId}/events/{eventId}
     *
     * @param eventId id редактируемого события
     * @param request UpdateEventUserRequest
     * @return EventFullDto
     */
    @PatchMapping("/{eventId}")
    public Object updateEvent(
            @PathVariable long eventId, @RequestBody @Valid UpdateEventUserRequest request, Principal principal) {

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
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/users/{userId}/events/{eventId}",
                        request, EventFullDto.class, userId, eventId));
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя.
     * Эндпоинт: GET /users/{userId}/events/{eventId}/requests
     *
     * @param eventId id события
     * @return List ParticipationRequestDto
     */
    @GetMapping("/{eventId}/requests")
    public Object getEventParticipation(@PathVariable long eventId, Principal principal) {
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return basicServiceRestTemplate.getForEntity("/users/{userId}/events/{eventId}/requests",
                ParticipationRequestDto[].class, userId, eventId);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя.
     * Эндпоинт: PATCH /users/{userId}/events/{eventId}/requests
     *
     * @param eventId id события
     * @param request EventRequestStatusUpdateRequest
     * @return EventRequestStatusUpdateResult
     */
    @PatchMapping("/{eventId}/requests")
    public Object updateEventState(
            @PathVariable long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request, Principal principal) {

        Optional.ofNullable(request.getStatus())
                .ifPresent(status -> {
                    if (!(status == ParticipationState.CONFIRMED || status == ParticipationState.REJECTED)) {
                        throw new BadRequestException("Invalid status");
                    }
                });
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return ResponseEntity.ok(basicServiceRestTemplate.patchForObject("/users/{userId}/events/{eventId}/requests",
                request, EventRequestStatusUpdateResult.class, userId, eventId));
    }
}
