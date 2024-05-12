package ru.practicum.explorewithme.basic.service.locations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.service.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.service.locations.services.PlaceService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlacePrivateController.class)
class PlacePrivateControllerTest {

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
    void createPlace_isAvailable() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n")
                .description("d".repeat(20))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();

        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(placeService).createPlace(eq(1L), any());
    }

    @Test
    @SneakyThrows
    void createPlace_ifInvalidName_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .description("d".repeat(20))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("").build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("       ").build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().name("n".repeat(51)).build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createPlace(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void createPlace_ifInvalidDescription_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("name")
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .radius(1000)
                .build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("").build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("       ").build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("n".repeat(19)).build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().description("n".repeat(7001)).build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createPlace(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void createPlace_ifInvalidLocation_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n".repeat(20))
                .description("d".repeat(200))
                .radius(1000)
                .build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder()
                .location(LocationDto.builder().build())
                .build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createPlace(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void createPlace_ifInvalidRadius_thenBadRequest() {
        NewPlaceDto body = NewPlaceDto.builder()
                .name("n".repeat(20))
                .description("d".repeat(200))
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().radius(-1).build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewPlaceDto.builder().radius(0).build();
        mockMvc.perform(post("/users/1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(placeService, never()).createPlace(anyLong(), any());
    }
}