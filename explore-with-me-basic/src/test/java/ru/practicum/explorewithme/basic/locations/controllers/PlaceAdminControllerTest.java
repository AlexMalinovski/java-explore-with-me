package ru.practicum.explorewithme.basic.locations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.services.PlaceService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceAdminController.class)
class PlaceAdminControllerTest {

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
    void createAndPublishPlace_isAvailable() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n")
                .description("d".repeat(20))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();

        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(placeService).createAndPublishPlace(any());
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifInvalidName_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .description("d".repeat(20))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("").build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("       ").build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("n".repeat(51)).build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createAndPublishPlace(any());
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifInvalidDescription_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("name")
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("").build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("       ").build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("n".repeat(19)).build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("n".repeat(7001)).build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createAndPublishPlace(any());
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifInvalidLocation_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n".repeat(20))
                .description("d".repeat(200))
                .radius(1000)
                .build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder()
                .location(LocationDto.builder().build())
                .build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createAndPublishPlace(any());
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifInvalidRadius_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n".repeat(20))
                .description("d".repeat(200))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().radius(-1).build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().radius(0).build();
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createAndPublishPlace(any());
    }

    @Test
    @SneakyThrows
    void updatePlaces_isAvailable() {
        ChangePlacesRequest body = ChangePlacesRequest.builder().placeIds(Set.of()).build();

        mockMvc.perform(patch("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(placeService).updatePlaces(any());
    }

    @Test
    @SneakyThrows
    void updatePlaces_ifInvalidPlaceIds_thenBadRequest() {
        ChangePlacesRequest body = ChangePlacesRequest.builder().build();

        mockMvc.perform(patch("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).updatePlaces(any());
    }

    @Test
    @SneakyThrows
    void updatePlace_isAvailable() {
        UpdatePlaceAdminRequest body = UpdatePlaceAdminRequest.builder().build();

        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(placeService).updatePlaceAdmin(eq(1L), any(UpdatePlaceAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updatePlace_ifInvalidName_thenBadRequest() {
        UpdatePlaceAdminRequest body = UpdatePlaceAdminRequest.builder().name("").build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().name(" ".repeat(20)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().name("n".repeat(51)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).updatePlaceAdmin(anyLong(), any(UpdatePlaceAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updatePlace_ifInvalidDescription_thenBadRequest() {
        UpdatePlaceAdminRequest body = UpdatePlaceAdminRequest.builder().description("").build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().description("\\n".repeat(2)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().description(" ".repeat(250)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().description("n".repeat(19)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().description("n".repeat(7001)).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).updatePlaceAdmin(anyLong(), any(UpdatePlaceAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updatePlace_ifInvalidLocation_thenBadRequest() {
        UpdatePlaceAdminRequest body = UpdatePlaceAdminRequest.builder()
                .location(LocationDto.builder().build())
                .build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).updatePlaceAdmin(anyLong(), any(UpdatePlaceAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updatePlace_ifInvalidRadius_thenBadRequest() {
        UpdatePlaceAdminRequest body = UpdatePlaceAdminRequest.builder().radius(-1).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdatePlaceAdminRequest.builder().radius(0).build();
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).updatePlaceAdmin(anyLong(), any(UpdatePlaceAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void deletePlace_isAvailable() {
        mockMvc.perform(delete("/admin/places/1"))
                .andExpect(status().isNoContent());

        verify(placeService).deletePlace(1L);
    }
}