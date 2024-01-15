package ru.practicum.explorewithme.statistics.service.visits;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.lib.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.service.visits.mappers.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.service.visits.mappers.ViewStatsMapper;
import ru.practicum.explorewithme.statistics.service.visits.models.EndpointHit;
import ru.practicum.explorewithme.statistics.service.visits.models.ViewStats;
import ru.practicum.explorewithme.statistics.service.visits.services.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    /**
     * Сохранение информации о том, что к эндпоинту был запрос.
     * Эндпоинт: POST /hit
     *
     * @param dto CreateEndpointHitDto
     * @return EndpointHitDto
     */
    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> createEndpointHit(@RequestBody @Valid CreateEndpointHitDto dto) {
        EndpointHit createdHit = statsService.createEndpointHit(
                endpointHitMapper.mapToEndpointHit(dto));
        return new ResponseEntity<>(endpointHitMapper.mapToEndpointHitDto(createdHit), HttpStatus.CREATED);
    }

    /**
     * Получение статистики по посещениям.
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате "yyyy-MM-dd HH:mm:ss")
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return List<ViewStatsDto>
     */
    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getViewStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {

        List<ViewStats> viewStats = statsService.getViewStats(start, end, uris, unique);
        return ResponseEntity.ok(viewStatsMapper.mapToViewStatsDto(viewStats));
    }
}
