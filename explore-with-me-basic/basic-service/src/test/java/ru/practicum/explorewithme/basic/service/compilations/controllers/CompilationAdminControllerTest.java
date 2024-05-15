package ru.practicum.explorewithme.basic.service.compilations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.lib.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.service.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.service.compilations.services.CompilationService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompilationAdminController.class)
class CompilationAdminControllerTest {

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
    void createCompilation_isAvailable() {
        NewCompilationDto body = NewCompilationDto.builder().title("title").build();

        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(compilationService).createCompilation(any());
    }

    @Test
    @SneakyThrows
    void createCompilation_ifInvalidTitle_thenIsBadRequest() {
        NewCompilationDto body = NewCompilationDto.builder().build();
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewCompilationDto.builder().title("").build();
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewCompilationDto.builder().title("a".repeat(51)).build();
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(compilationService, never()).createCompilation(any());
    }

    @Test
    @SneakyThrows
    void deleteCompilation_isAvailable() {
        mockMvc.perform(delete("/admin/compilations/1"))
                .andExpect(status().is(204));

        verify(compilationService).deleteCompilation(1L);
    }

    @Test
    @SneakyThrows
    void updateCompilation_isAvailable() {
        UpdateCompilationRequest body = UpdateCompilationRequest.builder().title("title").build();

        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(compilationService).updateCompilation(eq(1L), any(UpdateCompilationRequest.class));
    }

    @Test
    @SneakyThrows
    void updateCompilation_ifInvalidTitle_thenIsBadRequest() {
        UpdateCompilationRequest body = UpdateCompilationRequest.builder().title("").build();
        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateCompilationRequest.builder().title("t".repeat(51)).build();
        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(compilationService, never()).updateCompilation(anyLong(), any());
    }
}