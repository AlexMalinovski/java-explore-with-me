package ru.practicum.explorewithme.basic.compilations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.basic.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.basic.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.compilations.services.CompilationService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * Эндпоинт: POST /admin/compilations
     *
     * @param dto NewCompilationDto
     * @return CompilationDto
     */
    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody @Valid NewCompilationDto dto) {
        Compilation compilation = compilationService.createCompilation(
                compilationMapper.toCompilation(dto));
        return new ResponseEntity<>(compilationMapper.toCompilationDto(compilation), HttpStatus.CREATED);
    }

    /**
     * Удаление подборки.
     * Эндпоинт: DELETE /admin/compilations/{compId}
     *
     * @param compId id подборки
     * @return null body object
     */
    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable long compId) {
        compilationService.deleteCompilation(compId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * Обновить информацию о подборке.
     * Эндпоинт: PATCH /admin/compilations/{compId}
     *
     * @param compId  id подборки
     * @param request UpdateCompilationRequest
     * @return CompilationDto
     */
    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable long compId, @RequestBody @Valid UpdateCompilationRequest request) {

        Compilation updated = compilationService.updateCompilation(compId, request);
        return ResponseEntity.ok(compilationMapper.toCompilationDto(updated));
    }
}
