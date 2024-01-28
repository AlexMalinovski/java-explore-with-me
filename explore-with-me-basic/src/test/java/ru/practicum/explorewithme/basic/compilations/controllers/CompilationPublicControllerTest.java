package ru.practicum.explorewithme.basic.compilations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.compilations.services.CompilationService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompilationPublicController.class)
class CompilationPublicControllerTest {

    @MockBean
    private CompilationService compilationService;

    @MockBean
    private CompilationMapper compilationMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getCompilationsPublic_isAvailable() {
        mockMvc.perform(get("/compilations")
                        .param("pinned", "true")
                        .param("from", "10")
                        .param("size", "15"))
                .andExpect(status().isOk());

        verify(compilationService).getCompilationsPublic(true, 10, 15);
    }

    @Test
    @SneakyThrows
    void getCompilationsPublic_ifOptionalParametersNotPassed_invokeWithDefaultsValues() {
        mockMvc.perform(get("/compilations"))
                .andExpect(status().isOk());

        verify(compilationService).getCompilationsPublic(null, 0, 10);
    }

    @Test
    @SneakyThrows
    void getCompilationsPublic_ifInvalidFrom_thenBadRequest() {
        mockMvc.perform(get("/compilations")
                        .param("pinned", "true")
                        .param("from", "-1")
                        .param("size", "15"))
                .andExpect(status().isBadRequest());

        verify(compilationService, never()).getCompilationsPublic(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getCompilationsPublic_ifInvalidSize_thenBadRequest() {
        mockMvc.perform(get("/compilations")
                        .param("pinned", "true")
                        .param("from", "0")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/compilations")
                        .param("pinned", "true")
                        .param("from", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(compilationService, never()).getCompilationsPublic(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getCompilationById_isAvailable() {
        mockMvc.perform(get("/compilations/1"))
                .andExpect(status().isOk());

        verify(compilationService).getCompilationById(1L);
    }
}