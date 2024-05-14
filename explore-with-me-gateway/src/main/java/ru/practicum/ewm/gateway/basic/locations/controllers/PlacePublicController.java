package ru.practicum.ewm.gateway.basic.locations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceShortDto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/places")
@Validated
public class PlacePublicController {

    private final RestTemplate basicServiceRestTemplate;


    /**
     * Получение опубликованного места по его id.
     * Эндпойнт: GET /places/{placeId}
     *
     * @param placeId id места
     * @return PlaceDto
     */
    @GetMapping("/{placeId}")
    public Object getPublishedPlaceById(@PathVariable long placeId) {
        return basicServiceRestTemplate.getForEntity("/places/{placeId}", PlaceDto.class, placeId);
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
    public Object getPublishedPlacesByArea(
            @RequestParam @Valid @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") BigDecimal lat,
            @RequestParam @Valid @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") BigDecimal lon,
            @RequestParam(required = false, defaultValue = "2000") @Valid @Min(2000) @Max(20000) int searchRadius) {

        return basicServiceRestTemplate.getForEntity(
                "/places?lat={lat}&lon={lon}&searchRadius={searchRadius}", PlaceShortDto[].class, lat, lon, searchRadius);
    }
}
