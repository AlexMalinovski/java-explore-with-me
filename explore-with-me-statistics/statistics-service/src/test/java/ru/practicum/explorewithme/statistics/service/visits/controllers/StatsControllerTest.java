package ru.practicum.explorewithme.statistics.service.visits.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.statistics.lib.dto.CreateEndpointHitDto;
import ru.practicum.explorewithme.statistics.service.visits.controllers.StatsController;
import ru.practicum.explorewithme.statistics.service.visits.mappers.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.service.visits.mappers.ViewStatsMapper;
import ru.practicum.explorewithme.statistics.service.visits.services.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @MockBean
    private StatsService statsService;

    @MockBean
    private EndpointHitMapper endpointHitMapper;

    @MockBean
    private ViewStatsMapper viewStatsMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createEndpointHit_isAvailable() {
        CreateEndpointHitDto body = CreateEndpointHitDto.builder()
                .app("app")
                .uri("uri/1")
                .timestamp("2022-01-01 00:00:00")
                .ip("192.168.1.1")
                .build();
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(statsService).createEndpointHit(any());
    }

    @Test
    @SneakyThrows
    void getViewStats_isAvailable() {
        mockMvc.perform(get("/stats")
                        .param("start", "2022-01-01 00:00:00")
                        .param("end", "2023-01-01 00:00:00"))
                .andExpect(status().isOk());

        verify(statsService).getViewStats(
                LocalDateTime.parse("2022-01-01 00:00:00", DATE_FORMATTER),
                LocalDateTime.parse("2023-01-01 00:00:00", DATE_FORMATTER),
                null, false);
    }
}