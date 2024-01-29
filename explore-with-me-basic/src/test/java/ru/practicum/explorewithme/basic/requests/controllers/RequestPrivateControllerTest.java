package ru.practicum.explorewithme.basic.requests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.basic.requests.services.ParticipationService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestPrivateController.class)
class RequestPrivateControllerTest {

    @MockBean
    private ParticipationService participationService;

    @MockBean
    private ParticipationMapper participationMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createParticipation_isAvailable() {
        mockMvc.perform(post("/users/1/requests")
                        .param("eventId", "2"))
                .andExpect(status().isCreated());

        verify(participationService).createParticipation(1L, 2L);
    }

    @Test
    @SneakyThrows
    void getParticipation_isAvailable() {
        mockMvc.perform(get("/users/1/requests"))
                .andExpect(status().isOk());

        verify(participationService).getUserParticipation(1L);
    }

    @Test
    @SneakyThrows
    void cancelParticipation_isAvailable() {
        mockMvc.perform(patch("/users/1/requests/2/cancel"))
                .andExpect(status().isOk());

        verify(participationService).cancelParticipation(1L, 2L);
    }
}