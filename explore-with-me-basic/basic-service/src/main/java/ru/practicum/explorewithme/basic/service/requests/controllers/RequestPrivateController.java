package ru.practicum.explorewithme.basic.service.requests.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.basic.service.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;
import ru.practicum.explorewithme.basic.service.requests.services.ParticipationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
public class RequestPrivateController {

    private final ParticipationService participationService;
    private final ParticipationMapper participationMapper;

    /**
     * Добавление запроса от текущего пользователя на участие в событии.
     * Эндпоинт: POST /users/{userId}/requests
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return ParticipationRequestDto
     */
    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createParticipation(
            @PathVariable long userId, @RequestParam long eventId) {

        Participation created = participationService.createParticipation(userId, eventId);
        return new ResponseEntity<>(participationMapper.toParticipationRequestDto(created), HttpStatus.CREATED);
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях.
     * Эндпоинт: GET /users/{userId}/requests
     *
     * @param userId id текущего пользователя
     * @return List ParticipationRequestDto
     */
    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getParticipation(@PathVariable long userId) {

        List<Participation> participation = participationService.getUserParticipation(userId);
        return ResponseEntity.ok(participationMapper.toParticipationRequestDto(participation));
    }

    /**
     * Отмена своего запроса на участие в событии.
     * Эндпоинт: PATCH /users/{userId}/requests/{requestId}/cancel
     *
     * @param userId    id текущего пользователя
     * @param requestId id события
     * @return ParticipationRequestDto
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipation(@PathVariable long userId, @PathVariable long requestId) {

        Participation cancelled = participationService.cancelParticipation(userId, requestId);
        return ResponseEntity.ok(participationMapper.toParticipationRequestDto(cancelled));
    }
}
