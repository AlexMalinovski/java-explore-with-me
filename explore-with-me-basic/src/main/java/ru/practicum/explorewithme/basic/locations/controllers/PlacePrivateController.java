package ru.practicum.explorewithme.basic.locations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.basic.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.services.PlaceService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/places")
public class PlacePrivateController {

    private final PlaceService placeService;
    private final LocationMapper locationMapper;

    /**
     * Добавление нового места в статусе PENDING.
     * Эндпойнт: POST /users/{userId}/places
     *
     * @param userId id пользователя
     * @param dto    NewPlaceDto
     * @return PlaceDto
     */
    @PostMapping
    public ResponseEntity<PlaceDto> createPlace(@PathVariable Long userId, @RequestBody @Valid NewPlaceDto dto) {
        Place place = placeService.createPlace(userId, locationMapper.mapToPlace(dto));
        return new ResponseEntity<>(locationMapper.mapToPlaceDto(place), HttpStatus.CREATED);
    }
}
