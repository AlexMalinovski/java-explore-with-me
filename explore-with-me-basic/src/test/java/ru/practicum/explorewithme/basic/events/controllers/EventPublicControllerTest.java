package ru.practicum.explorewithme.basic.events.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.services.EventService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPublicController.class)
class EventPublicControllerTest {

    @MockBean
    private EventService eventService;

    @MockBean
    private EventMapper eventMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getEventById_isAvailable() {
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk());

        verify(eventService).getPublishEventById(1L);
    }

    @Test
    @SneakyThrows
    void getEventById_ifSuccess_thenPostHitToStatistics() {
        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk());

        verify(statsClient).postHit(anyString(), eq("/events/1"), anyString(), any(LocalDateTime.class));
    }

    @Test
    @SneakyThrows
    void getEventsPublic_isAvailable() {
        mockMvc.perform(get("/events")
                        .param("text", "text")
                        .param("categories", "1")
                        .param("paid", "true")
                        .param("onlyAvailable", "true")
                        .param("sort", "sort")
                        .param("rangeStart", "2000-01-01 00:00:00")
                        .param("rangeEnd", "2100-01-01 00:00:00")
                        .param("from", "0")
                        .param("size", "50"))
                .andExpect(status().isOk());

        verify(eventService).getEventsPublic(any(GetEventRequest.class), eq(0), eq(50));
    }

    @Test
    @SneakyThrows
    void getEventsPublic_ifOptionalParametersNotPassed_isAvailable() {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());

        verify(eventService).getEventsPublic(any(GetEventRequest.class), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEventsPublic_setDefaultParam() {
        ArgumentCaptor<GetEventRequest> captor = ArgumentCaptor.forClass(GetEventRequest.class);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());


        verify(eventService).getEventsPublic(captor.capture(), eq(0), eq(10));
        assertEquals(false, captor.getValue().getOnlyAvailable());
    }

    @Test
    @SneakyThrows
    void getEventsPublic_ifInvalidRange_thenBadRequest() {
        mockMvc.perform(get("/events")
                        .param("rangeStart", "2022-01-01 00:00:00")
                        .param("rangeEnd", "2021-01-01 00:00:00"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/events")
                        .param("rangeStart", "2022-01-01 00:00:00")
                        .param("rangeEnd", "2022-01-01 00:00:00"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEventsPublic(any(GetEventRequest.class), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEventsPublic_ifInvalidFrom_thenBadRequest() {
        mockMvc.perform(get("/events")
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEventsPublic(any(GetEventRequest.class), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEventsPublic_ifInvalidSize_thenBadRequest() {
        mockMvc.perform(get("/events")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/events")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEventsPublic(any(GetEventRequest.class), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEventsPublic_ifSuccess_thenPostHitToStatistics() {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());

        verify(statsClient).postHit(anyString(), eq("/events"), anyString(), any(LocalDateTime.class));
    }
}