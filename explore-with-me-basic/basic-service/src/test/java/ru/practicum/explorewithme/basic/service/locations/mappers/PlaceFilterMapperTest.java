package ru.practicum.explorewithme.basic.service.locations.mappers;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.CircleSearchArea;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceSort;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlaceFilterMapperTest {

    private final PlaceFilterMapper mapper = new PlaceFilterMapperImpl();

    @Test
    void toPlaceFilter_ifRequestNull_thenReturnDefault() {
        BooleanExpression actual = mapper.toPlaceFilter(null);

        assertEquals(Expressions.TRUE.isTrue(), actual);
    }

    @Test
    void toPlaceFilter_ifName() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().name("name").build();

        var actual = mapper.toPlaceFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && lower(place.name) like %name%", actual.toString());
    }

    @Test
    void toPlaceFilter_ifDescription() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().description("name").build();

        var actual = mapper.toPlaceFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && lower(place.description) like %name%", actual.toString());
    }

    @Test
    void toPlaceFilter_ifStatus() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().statuses(Set.of(PlaceStatus.PENDING)).build();

        var actual = mapper.toPlaceFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && place.status = PENDING", actual.toString());
    }

    @Test
    void toPlaceFilter_ifArea() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder()
                .area(new CircleSearchArea(50.5, 30.2, 1000))
                .build();

        var actual = mapper.toPlaceFilter(expected);

        assertNotNull(actual);
        assertEquals("true = true && function('earth_distance', 50.5, 30.2, place.location.lat, place.location.lon) <= 1000", actual.toString());
    }

    @Test
    void toPlaceOrder_ifRequestNull_thenDefault() {
        var actual = mapper.toPlaceOrder(null);
        assertNotNull(actual);
        assertEquals("place.id ASC", actual.toString());

        actual = mapper.toPlaceOrder(PlaceFilterRequest.builder().orderBy(null).build());
        assertNotNull(actual);
        assertEquals("place.id ASC", actual.toString());
    }

    @Test
    void toPlaceOrder_ifNAME_ASC() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().orderBy(PlaceSort.NAME_ASC).build();

        var actual = mapper.toPlaceOrder(expected);

        assertNotNull(actual);
        assertEquals("place.name ASC", actual.toString());
    }

    @Test
    void toPlaceOrder_ifNAME_DESC() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().orderBy(PlaceSort.NAME_DESC).build();

        var actual = mapper.toPlaceOrder(expected);

        assertNotNull(actual);
        assertEquals("place.name DESC", actual.toString());
    }

    @Test
    void toPlaceOrder_ifSTATUS_DESC() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().orderBy(PlaceSort.STATUS_DESC).build();

        var actual = mapper.toPlaceOrder(expected);

        assertNotNull(actual);
        assertEquals("place.status DESC", actual.toString());
    }

    @Test
    void toPlaceOrder_ifSTATUS_ASC() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().orderBy(PlaceSort.STATUS_ASC).build();

        var actual = mapper.toPlaceOrder(expected);

        assertNotNull(actual);
        assertEquals("place.status ASC", actual.toString());
    }

    @Test
    void toPlaceOrder_ifDEFAULT() {
        PlaceFilterRequest expected = PlaceFilterRequest.builder().orderBy(PlaceSort.DEFAULT).build();

        var actual = mapper.toPlaceOrder(expected);

        assertNotNull(actual);
        assertEquals("place.id ASC", actual.toString());
    }
}