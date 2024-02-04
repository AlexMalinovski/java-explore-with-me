package ru.practicum.explorewithme.basic.locations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.locations.dto.PlaceShortDto;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.services.PlaceService;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/places")
@Validated
public class PlacePublicController {

    private final PlaceService placeService;
    private final LocationMapper locationMapper;


    /**
     * Получение опубликованного места по его id.
     * Эндпойнт: GET /places/{placeId}
     *
     * @param placeId id места
     * @return PlaceDto
     */
    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceDto> getPublishedPlaceById(@PathVariable long placeId) {
        Place place = placeService.getPlaceById(placeId);
        if (place.getStatus() != PlaceStatus.PUBLISHED) {
            throw new NotFoundException("Place not found");
        }
        return ResponseEntity.ok(locationMapper.mapToPlaceDto(place));
    }

    /**
     * Получение основной информации об опубликованных местах, расположенных в окрестности заданной геогр. точки POI.
     * Эндпойнт: GET GET /places
     *
     * @param lat          градусов, широта POI в десятичном представлении
     * @param lon          градусов, долгота POI в десятичном представлении
     * @param searchRadius метров, радиус поиска
     * @return List PlaceShortDto
     */
    @GetMapping
    public ResponseEntity<List<PlaceShortDto>> getPublishedPlacesByArea(
            @RequestParam @Valid @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") BigDecimal lat,
            @RequestParam @Valid @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") BigDecimal lon,
            @RequestParam(required = false, defaultValue = "2000") @Valid @Min(2000) @Max(20000) int searchRadius) {

        PlaceFilterRequest request = PlaceFilterRequest.builder()
                .area(locationMapper.mapToCircleSearchArea(lat, lon, searchRadius))
                .statuses(Set.of(PlaceStatus.PUBLISHED))
                .build();
        List<Place> places = placeService.getPlaces(request);

        return ResponseEntity.ok(locationMapper.mapToPlaceShortDto(places));
    }
}
