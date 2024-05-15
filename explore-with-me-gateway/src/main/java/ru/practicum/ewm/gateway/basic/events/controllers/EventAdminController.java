package ru.practicum.ewm.gateway.basic.events.controllers;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.exceptions.api.BadRequestException;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.EventFullDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.StateAction;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events")
@Validated
public class EventAdminController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Редактирование данных события и его статуса (отклонение/публикация).
     * Эндпоинт: PATH /admin/events/{eventId}
     *
     * @param eventId id события
     * @param request UpdateEventAdminRequest
     * @return EventFullDto
     */
    @PatchMapping("/{eventId}")
    public Object updateEvent(@PathVariable long eventId,
                              @RequestBody @Valid UpdateEventAdminRequest request) {

        Optional.ofNullable(request.getStateAction())
                .ifPresent(action -> {
                    if (!(action == StateAction.PUBLISH_EVENT || action == StateAction.REJECT_EVENT)) {
                        throw new BadRequestException("Invalid action");
                    }
                });

        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/admin/events/{eventId}",
                        request, EventFullDto.class, eventId));
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
    public Object getEventsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size,
            WebRequest webRequest) {


        Map<String, String[]> allParam = webRequest.getParameterMap();
        StringBuilder sb = new StringBuilder("/admin/events?");
        allParam.forEach((key, value) -> sb.append(String.format("%s={%s}&", key, key)));

        return basicServiceRestTemplate.getForEntity(sb.toString(), EventFullDto[].class, allParam);
    }


}
