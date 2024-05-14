package ru.practicum.ewm.gateway.basic.requests.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.ParticipationRequestDto;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/requests")
@Validated
public class RequestPrivateController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Добавление запроса от текущего пользователя на участие в событии.
     * Эндпоинт: POST /users/{userId}/requests
     *
     * @param eventId id события
     * @return ParticipationRequestDto
     */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createParticipation(@RequestParam long eventId, Principal principal) {

        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");

        return basicServiceRestTemplate.postForEntity("/users/{userId}/requests?eventId={eventId}", null, ParticipationRequestDto.class, userId, eventId);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях.
     * Эндпоинт: GET /users/{userId}/requests
     *
     * @return List ParticipationRequestDto
     */
    @GetMapping
    public Object getParticipation(Principal principal) {

        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");
        return basicServiceRestTemplate.getForEntity(
                "/users/{userId}/requests", ParticipationRequestDto[].class, userId);
    }

    /**
     * Отмена своего запроса на участие в событии.
     * Эндпоинт: PATCH /users/{userId}/requests/{requestId}/cancel
     *
     * @param requestId id события
     * @return ParticipationRequestDto
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipation(@PathVariable long requestId, Principal principal) {

        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");
        return ResponseEntity.ok(basicServiceRestTemplate.patchForObject(
                "/users/{userId}/requests/{requestId}/cancel", null, ParticipationRequestDto.class,
                userId, requestId));
    }
}
