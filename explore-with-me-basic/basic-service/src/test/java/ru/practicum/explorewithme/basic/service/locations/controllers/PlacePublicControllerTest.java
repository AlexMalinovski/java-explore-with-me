package ru.practicum.explorewithme.basic.service.locations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.CircleSearchArea;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.service.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.service.locations.models.Place;
import ru.practicum.explorewithme.basic.service.locations.services.PlaceService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlacePublicController.class)
class PlacePublicControllerTest {

    @MockBean
    private PlaceService placeService;

    @MockBean
    private LocationMapper locationMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getPublishedPlaceById_ifPlaceNotPublished_thenNotFound() {
        when(placeService.getPlaceById(anyLong())).thenReturn(Place.builder().build());

        for (PlaceStatus status : PlaceStatus.values()) {
            if (status == PlaceStatus.PUBLISHED) {
                continue;
            }
            mockMvc.perform(get("/places/1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @SneakyThrows
    void getPublishedPlaceById_isAvailable() {
        when(placeService.getPlaceById(anyLong())).thenReturn(Place.builder().status(PlaceStatus.PUBLISHED).build());

        mockMvc.perform(get("/places/1"))
                .andExpect(status().isOk());

        verify(placeService).getPlaceById(1L);
    }

    @Test
    @SneakyThrows
    void getPublishedPlacesByArea_isAvailable() {
        CircleSearchArea expectedArea = new CircleSearchArea(20.2, 30.2, 3000);
        ArgumentCaptor<PlaceFilterRequest> captor = ArgumentCaptor.forClass(PlaceFilterRequest.class);
        when(locationMapper.mapToCircleSearchArea(any(BigDecimal.class), any(BigDecimal.class), anyInt())).thenReturn(expectedArea);

        mockMvc.perform(get("/places")
                        .param("lat", "20.2")
                        .param("lon", "30.2")
                        .param("searchRadius", "3000"))
                .andExpect(status().isOk());

        verify(locationMapper).mapToCircleSearchArea(new BigDecimal("20.2"), new BigDecimal("30.2"), 3000);
        verify(placeService).getPlaces(captor.capture());
        PlaceFilterRequest filter = captor.getValue();
        assertNotNull(filter);
        assertEquals(Set.of(PlaceStatus.PUBLISHED), filter.getStatuses());
        assertEquals(expectedArea, filter.getArea());
    }

    @Test
    @SneakyThrows
    void getPublishedPlacesByArea_defaultParam() {
        mockMvc.perform(get("/places")
                        .param("lat", "20.2")
                        .param("lon", "20.2"))
                .andExpect(status().isOk());

        verify(locationMapper).mapToCircleSearchArea(any(BigDecimal.class), any(BigDecimal.class), eq(2000));
    }

    @Test
    @SneakyThrows
    void getPublishedPlacesByArea_ifInvalidLat_thenBadRequest() {
        mockMvc.perform(get("/places")
                        .param("lon", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lat", "")
                        .param("lon", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lat", "-90.00000000000000000000001")
                        .param("lon", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lat", "90.00000000000000000000001")
                        .param("lon", "20.2"))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).getPlaces(any());
    }

    @Test
    @SneakyThrows
    void getPublishedPlacesByArea_ifInvalidLon_thenBadRequest() {
        mockMvc.perform(get("/places")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lon", "")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lon", "-180.00000000000000000000001")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("lon", "180.00000000000000000000001")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).getPlaces(any());
    }

    @Test
    @SneakyThrows
    void getPublishedPlacesByArea_ifInvalidSearchRadius_thenBadRequest() {
        mockMvc.perform(get("/places")
                        .param("searchRadius", "0")
                        .param("lon", "28.2")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("searchRadius", "1999")
                        .param("lon", "28.2")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/places")
                        .param("searchRadius", "20001")
                        .param("lon", "28.2")
                        .param("lat", "20.2"))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).getPlaces(any());
    }
}