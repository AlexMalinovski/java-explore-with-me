package ru.practicum.explorewithme.basic.locations.controllers;

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
import ru.practicum.explorewithme.basic.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.locations.enums.PlaceSort;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.services.PlaceService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/places")
public class PlaceAdminController {

    private final PlaceService placeService;
    private final LocationMapper locationMapper;

    /**
     * Создание и пубоикация нового места.
     * Эндпойнт: POST /admin/places
     *
     * @param dto NewPlaceDto
     * @return PlaceDto
     */
    @PostMapping
    public ResponseEntity<PlaceDto> createAndPublishPlace(@RequestBody @Valid NewPlaceDto dto) {
        Place place = placeService.createAndPublishPlace(locationMapper.mapToPlace(dto));
        return new ResponseEntity<>(locationMapper.mapToPlaceDto(place), HttpStatus.CREATED);
    }

    /**
     * Редактирование мест (пакетная операция).
     * Эндпойнт: PATCH /admin/places
     *
     * @param request ChangePlacesRequest
     * @return List PlaceDto
     */
    @PatchMapping
    public ResponseEntity<List<PlaceDto>> updatePlaces(@RequestBody @Valid ChangePlacesRequest request) {
        List<Place> place = placeService.updatePlaces(request);
        return ResponseEntity.ok(locationMapper.mapToPlaceDto(place));
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
    public ResponseEntity<PlaceDto> updatePlaceById(
            @PathVariable long placeId, @RequestBody @Valid UpdatePlaceAdminRequest request) {
        Place place = placeService.updatePlaceAdmin(placeId, request);
        return ResponseEntity.ok(locationMapper.mapToPlaceDto(place));
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
        placeService.deletePlace(placeId);
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
        Place place = placeService.getPlaceById(placeId);
        return ResponseEntity.ok(locationMapper.mapToPlaceDto(place));
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
    public ResponseEntity<List<PlaceDto>> getPlaces(
            @RequestParam(required = false) Set<PlaceStatus> statuses,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "DEFAULT") PlaceSort orderBy,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @Valid @Positive int size) {

        PlaceFilterRequest request = PlaceFilterRequest.builder()
                .statuses(statuses)
                .name(name)
                .description(description)
                .orderBy(orderBy)
                .build();
        List<Place> places = placeService.getPlaces(request, from, size);

        return ResponseEntity.ok(locationMapper.mapToPlaceDto(places));
    }
}
