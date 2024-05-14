package ru.practicum.ewm.gateway.basic.compilations.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.gateway.configs.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompilationPublicController.class)
@Import(SecurityConfig.class)
class CompilationPublicControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getCompilationsPublic_whenAnonymous_thenOk() {
        mockMvc.perform(get("/public/compilations"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getCompilationById_whenAnonymous_thenOk() {
        mockMvc.perform(get("/public/compilations/1"))
                .andExpect(status().isOk());
    }
}