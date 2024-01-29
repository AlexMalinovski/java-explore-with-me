package ru.practicum.explorewithme.basic.compilations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.basic.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.compilations.services.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

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
    public ResponseEntity<List<CompilationDto>> getCompilationsPublic(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        List<Compilation> compilations = compilationService.getCompilationsPublic(pinned, from, size);
        return ResponseEntity.ok(compilationMapper.toCompilationDto(compilations));

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
        Compilation compilation = compilationService.getCompilationById(compId);
        return ResponseEntity.ok(compilationMapper.toCompilationDto(compilation));
    }
}
