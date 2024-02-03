package ru.practicum.explorewithme.basic.locations.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.models.embeddable.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PlaceServiceItTest {

    @Autowired
    private PlaceService placeService;

    @Test
    void createAndPublishPlace() {
        Place newPlace = Place.builder()
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(Location.builder()
                        .lat(25.2)
                        .lon(50.5)
                        .build())
                .radius(1000)
                .build();

        Place created = placeService.createAndPublishPlace(newPlace);
        assertThrows(DataIntegrityViolationException.class, () -> placeService.createAndPublishPlace(newPlace));

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(newPlace.getStatus(), created.getStatus());
        assertEquals(newPlace.getName(), created.getName());
        assertEquals(newPlace.getDescription(), created.getDescription());
        assertEquals(newPlace.getLocation(), created.getLocation());
        assertEquals(newPlace.getRadius(), created.getRadius());
    }
}