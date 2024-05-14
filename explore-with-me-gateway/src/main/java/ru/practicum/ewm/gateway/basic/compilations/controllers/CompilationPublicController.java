package ru.practicum.ewm.gateway.basic.compilations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.compilations.CompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/public/compilations")
public class CompilationPublicController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Получение подборок событий.
     * Эндпоинт: GET /compilations
     *
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return List CompilationDto
     */
    @GetMapping
    public Object getCompilationsPublic(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        return basicServiceRestTemplate.getForEntity("/compilations?pinned={pinned}&from={from}&size={size}",
                CompilationDto[].class, pinned, from, size);
    }

    /**
     * Получение подборки событий по его id.
     * Эндпоинт: GET /compilations/{compId}
     *
     * @param compId id подборки
     * @return CompilationDto
     */
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable long compId) {

        return basicServiceRestTemplate.getForEntity("/compilations/{compId}", CompilationDto.class, compId);
    }
}
