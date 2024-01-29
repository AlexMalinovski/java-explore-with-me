package ru.practicum.explorewithme.basic.events.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.services.EventService;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventAdminController.class)
class EventAdminControllerTest {

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
    void updateEvent_isAvailable() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().title("title").build();

        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(eventService).updateEvent(eq(1L), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidAnnotation_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().annotation("").build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().annotation("a".repeat(19)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().annotation("a".repeat(2001)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidCategory_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().category(-1L).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().category(0L).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidDescription_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().description("").build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().description("a".repeat(19)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().description("a".repeat(7001)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidEventDate_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().eventDate(LocalDateTime.now().minusSeconds(1)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidLocationLat_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("-90.000001")).lon(new BigDecimal("100.0")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("90.000001")).lon(new BigDecimal("100.0")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("20.0000009")).lon(new BigDecimal("100.0")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(null).lon(new BigDecimal("100.0")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());


        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidLocationLon_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("20.1")).lon(new BigDecimal("-180.000001")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("20.01")).lon(new BigDecimal("180.000001")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("20.9")).lon(new BigDecimal("100.0000001")).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().location(LocationDto.builder().lat(new BigDecimal("20.9")).lon(null).build()).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());


        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidTitle_thenBadRequest() {
        UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().title("").build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().title("a".repeat(2)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventAdminRequest.builder().title("a".repeat(121)).build();
        mockMvc.perform(patch("/admin/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidStateAction_thenBadRequest() {
        for (StateAction action : StateAction.values()) {
            if (action == StateAction.PUBLISH_EVENT || action == StateAction.REJECT_EVENT) {
                continue;
            }
            UpdateEventAdminRequest body = UpdateEventAdminRequest.builder().stateAction(action).build();
            mockMvc.perform(patch("/admin/events/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(body)))
                    .andExpect(status().isBadRequest());
        }

        verify(eventService, never()).updateEvent(anyLong(), any(UpdateEventAdminRequest.class));
    }

    @Test
    @SneakyThrows
    void getEventsAdmin_isAvailable() {
        mockMvc.perform(get("/admin/events")
                        .param("users", "1")
                        .param("states", "PENDING")
                        .param("categories", "2")
                        .param("rangeStart", "2020-01-01 00:00:00")
                        .param("rangeEnd", "2028-01-01 00:00:00")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(eventService).getEventsAdmin(any(GetEventRequest.class), eq(0), eq(10));
    }

    @Test
    @SneakyThrows
    void getEventsAdmin_ifOptionalParametersNotPassed_thenAvailable() {
        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk());

        verify(eventService).getEventsAdmin(any(GetEventRequest.class), eq(0), eq(10));
    }

    @Test
    @SneakyThrows
    void getEventsAdmin_ifInvalidParameters_thenBadRequest() {
        mockMvc.perform(get("/admin/events")
                        .param("users", "abc"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("categories", "abc"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("rangeStart", "2020-01-01T00:00:00"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("rangeEnd", "2020-01-01T00:00:00"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/events")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEventsAdmin(any(GetEventRequest.class), anyInt(), anyInt());
    }
}