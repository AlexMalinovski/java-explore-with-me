package ru.practicum.ewm.gateway.basic.locations.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceDto;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/places")
public class PlacePrivateController {

    private final RestTemplate basicServiceRestTemplate;

    /**
     * Добавление нового места в статусе PENDING.
     * Эндпойнт: POST /users/{userId}/places
     *
     * @param dto NewPlaceDto
     * @return PlaceDto
     */
    @PostMapping
    public Object createPlace(@RequestBody @Valid NewPlaceDto dto, Principal principal) {
        Object userId = ((JwtAuthenticationToken) principal)
                .getTokenAttributes()
                .get("userId");
        return basicServiceRestTemplate.postForEntity("/users/{userId}/places", dto, PlaceDto.class, userId);
    }
}
