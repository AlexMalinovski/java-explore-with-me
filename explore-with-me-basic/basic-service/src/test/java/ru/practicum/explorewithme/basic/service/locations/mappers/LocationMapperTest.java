package ru.practicum.explorewithme.basic.service.locations.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.service.locations.models.Place;
import ru.practicum.explorewithme.basic.service.locations.models.embeddable.Location;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LocationMapperTest {

    private final LocationMapper mapper = new LocationMapperImpl();

    @Test
    void mapToLocationDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToLocationDto(null);
        assertNull(actual);
    }

    @Test
    void mapToLocationDto() {
        Location expected = Location.builder()
                .lat(22.123489d)
                .lon(-122.111189d)
                .build();

        var actual = mapper.mapToLocationDto(expected);

        assertNotNull(actual);
        assertEquals(Double.toString(expected.getLat()), Double.toString(actual.getLat().doubleValue()));
        assertEquals(Double.toString(expected.getLon()), Double.toString(actual.getLon().doubleValue()));
    }

    @Test
    void mapToLocationDto_ifZeroSources_thenZeroTarget() {
        Location expected = Location.builder()
                .lat(0d)
                .lon(0d)
                .build();

        var actual = mapper.mapToLocationDto(expected);

        assertNotNull(actual);
        assertEquals("0.0", Double.toString(actual.getLat().doubleValue()));
        assertEquals("0.0", Double.toString(actual.getLon().doubleValue()));
    }

    @Test
    void mapToLocationDto_ifSmallPrecisionSources_thenZeroTarget() {
        Location expected = Location.builder()
                .lat(0.0000009d)
                .lon(0.0000005d)
                .build();

        var actual = mapper.mapToLocationDto(expected);

        assertNotNull(actual);
        assertEquals("0.0", Double.toString(actual.getLat().doubleValue()));
        assertEquals("0.0", Double.toString(actual.getLon().doubleValue()));
    }

    @Test
    void mapToLocationDto_ifVeryPrecisionSources_thenAccurateTo6Digits() {
        Location expected = Location.builder()
                .lat(0.123456789d)
                .lon(-100.123456789d)
                .build();

        var actual = mapper.mapToLocationDto(expected);

        assertNotNull(actual);
        assertEquals("0.123456", Double.toString(actual.getLat().doubleValue()));
        assertEquals("-100.123456", Double.toString(actual.getLon().doubleValue()));
    }

    @Test
    void mapToLocation_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToLocation(null);
        assertNull(actual);
    }

    @Test
    void mapToLocation() {
        LocationDto expected = LocationDto.builder()
                .lat(new BigDecimal("22.123489"))
                .lon(new BigDecimal("-122.111189"))
                .build();

        var actual = mapper.mapToLocation(expected);

        assertNotNull(actual);
        assertEquals("22.123489", Double.toString(actual.getLat()));
        assertEquals("-122.111189", Double.toString(actual.getLon()));
    }

    @Test
    void mapToPlace_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToPlace(null);
        assertNull(actual);
    }

    @Test
    void mapToPlace() {
        NewPlaceDto expected = NewPlaceDto.builder()
                .name("name")
                .description("description")
                .location(LocationDto.builder().build())
                .radius(1000)
                .build();

        var actual = mapper.mapToPlace(expected);

        assertNotNull(actual);
        assertNull(actual.getId());
        assertNotNull(actual.getLocation());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getRadius(), actual.getRadius());
        assertEquals(PlaceStatus.PENDING, actual.getStatus());
    }

    @Test
    void mapToPlaceDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToPlaceDto((Place) null);
        assertNull(actual);
    }

    @Test
    void mapToPlaceDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapToPlaceDto((List<Place>) null);
        assertNull(actual);
    }

    @Test
    void mapToPlaceDto() {
        Place expected = Place.builder()
                .id(1L)
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(Location.builder().build())
                .radius(1000)
                .build();

        var actual = mapper.mapToPlaceDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertNotNull(actual.getLocation());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getRadius(), actual.getRadius());
        assertEquals(expected.getStatus().name(), actual.getStatus());
    }

    @Test
    void mapToPlaceDto_ifSrcList() {
        List<Place> expected = List.of(Place.builder()
                .id(1L)
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(Location.builder().build())
                .radius(1000)
                .build());

        var actual = mapper.mapToPlaceDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertNotNull(actual.get(0).getLocation());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getRadius(), actual.get(0).getRadius());
        assertEquals(expected.get(0).getStatus().name(), actual.get(0).getStatus());
    }

    @Test
    void mapToPlaceShortDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToPlaceShortDto((Place) null);
        assertNull(actual);
    }

    @Test
    void mapToPlaceShortDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapToPlaceShortDto((List<Place>) null);
        assertNull(actual);
    }

    @Test
    void mapToPlaceShortDto() {
        Place expected = Place.builder()
                .id(1L)
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(Location.builder().build())
                .radius(1000)
                .build();

        var actual = mapper.mapToPlaceShortDto(expected);

        assertNotNull(actual);
        assertNotNull(actual.getLocation());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void mapToPlaceShortDto_ifSrcList() {
        List<Place> expected = List.of(Place.builder()
                .id(1L)
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(Location.builder().build())
                .radius(1000)
                .build());

        var actual = mapper.mapToPlaceShortDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertNotNull(actual.get(0).getLocation());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
    }

    @Test
    void mapToCircleSearchArea() {
        BigDecimal lat = new BigDecimal("50.1");
        BigDecimal lon = new BigDecimal("40.1");
        int radius = 100;

        var actual = mapper.mapToCircleSearchArea(lat, lon, radius);

        assertNotNull(actual);
        assertEquals(lat.doubleValue(), actual.getLat());
        assertEquals(lon.doubleValue(), actual.getLon());
        assertEquals(radius, actual.getRadius());
    }

    @Test
    void mapToCircleSearchArea_ifLatLonNull() {
        BigDecimal value = new BigDecimal("20.1");
        int radius = 100;
        var actual = mapper.mapToCircleSearchArea(null, null, radius);
        assertNull(actual);

        actual = mapper.mapToCircleSearchArea(null, value, radius);
        assertNotNull(actual);
        assertNull(actual.getLat());
        assertNotNull(actual.getLon());

        actual = mapper.mapToCircleSearchArea(value, null, radius);
        assertNotNull(actual);
        assertNull(actual.getLon());
        assertNotNull(actual.getLat());
    }

    @Test
    void updatePlace() {
        Place expectedSrc = Place.builder()
                .id(1L)
                .status(PlaceStatus.PUBLISHED)
                .name("name")
                .description("description")
                .location(null)
                .radius(1000)
                .build();

        UpdatePlaceAdminRequest expectedUpd = UpdatePlaceAdminRequest.builder()
                .status(PlaceStatus.REJECTED)
                .name("new name")
                .description("new description")
                .location(LocationDto.builder().build())
                .radius(2000)
                .build();

        var actual = mapper.updatePlace(expectedSrc, expectedUpd);

        assertNotNull(actual);
        assertEquals(expectedSrc.getId(), actual.getId());
        assertEquals(expectedUpd.getStatus(), actual.getStatus());
        assertEquals(expectedUpd.getName(), actual.getName());
        assertEquals(expectedUpd.getDescription(), actual.getDescription());
        assertEquals(expectedUpd.getRadius(), actual.getRadius());
        assertNotNull(actual.getLocation());
    }
}