package ru.practicum.explorewithme.basic.locations.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.locations.models.embeddable.Location;

import java.math.BigDecimal;

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
}