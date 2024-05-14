package ru.practicum.ewm.gateway.basic.compilations.controllers;

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
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.compilations.CompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Добавление новой подборки (подборка может не содержать событий)
     * Эндпоинт: POST /admin/compilations
     *
     * @param dto NewCompilationDto
     * @return CompilationDto
     */
    @PostMapping
    public Object createCompilation(@RequestBody @Valid NewCompilationDto dto) {

        return basicServiceRestTemplate.postForEntity("/admin/compilations", dto, CompilationDto.class);
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
        basicServiceRestTemplate.delete("/admin/compilations/{compId}", compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
    public Object updateCompilation(@PathVariable long compId, @RequestBody @Valid UpdateCompilationRequest request) {

        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/admin/compilations/{compId}", request, CompilationDto.class, compId));
    }
}
