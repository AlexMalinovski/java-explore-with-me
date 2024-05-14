package ru.practicum.ewm.gateway.basic.locations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceSort;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/places")
public class PlaceAdminController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Создание и пубоикация нового места.
     * Эндпойнт: POST /admin/places
     *
     * @param dto NewPlaceDto
     * @return PlaceDto
     */
    @PostMapping
    public Object createAndPublishPlace(@RequestBody @Valid NewPlaceDto dto) {
        return basicServiceRestTemplate.postForEntity("/admin/places", dto, PlaceDto.class);
    }

    /**
     * Редактирование мест (пакетная операция).
     * Эндпойнт: PATCH /admin/places
     *
     * @param request ChangePlacesRequest
     * @return List PlaceDto
     */
    @PatchMapping
    public Object updatePlaces(@RequestBody @Valid ChangePlacesRequest request) {
        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/admin/places", request, PlaceDto[].class));
    }

    /**
     * Редактирование места по его id.
     * Эндпойнт: PATCH /admin/places/{placeId}
     *
     * @param placeId id места
     * @param request UpdatePlaceAdminRequest
     * @return PlaceDto
     */
    @PatchMapping("/{placeId}")
    public Object updatePlaceById(
            @PathVariable long placeId, @RequestBody @Valid UpdatePlaceAdminRequest request) {

        return ResponseEntity.ok(
                basicServiceRestTemplate.patchForObject("/admin/places/{placeId}", request, PlaceDto.class, placeId));
    }

    /**
     * Удаление места по его id.
     * Эндпойнт: DELETE /admin/places/{placeId}
     *
     * @param placeId id места
     * @return no body
     */
    @DeleteMapping("/{placeId}")
    public ResponseEntity<Object> deletePlace(@PathVariable long placeId) {
        basicServiceRestTemplate.delete("/admin/places/{placeId}", placeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Получение места по его id.
     * Эндпойнт: GET /admin/places/{placeId}
     *
     * @param placeId id места
     * @return PlaceDto
     */
    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable long placeId) {

        return basicServiceRestTemplate.getForEntity("/admin/places/{placeId}", PlaceDto.class, placeId);
    }

    /**
     * Получение списка мест с возможностью фильтрации по полям.
     * Эндпойнт: GET /admin/places
     *
     * @param statuses    список статусов
     * @param name        имя - подстрока
     * @param description описание - подстрока
     * @param orderBy     порядок сортировки [DEFAULT, NAME_ASC, NAME_DESC, STATUS_ASC, STATUS_DESC]
     * @param from        количество мест, которые нужно пропустить для формирования текущего набора
     * @param size        количество мест в наборе
     * @return List PlaceDto
     */
    @GetMapping
    public Object getPlaces(
            @RequestParam(required = false) Set<PlaceStatus> statuses,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "DEFAULT") PlaceSort orderBy,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size,
            WebRequest webRequest) {

        Map<String, String[]> allParam = webRequest.getParameterMap();
        StringBuilder sb = new StringBuilder("/admin/places?");
        allParam.forEach((key, value) -> sb.append(String.format("%s={%s}&", key, key)));

        return basicServiceRestTemplate.getForEntity(sb.toString(), PlaceDto[].class, allParam);
    }
}
