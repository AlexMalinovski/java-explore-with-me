package ru.practicum.ewm.gateway.basic.requests.controllers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.gateway.configs.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RequestPrivateController.class)
@Import(SecurityConfig.class)
class RequestPrivateControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void createParticipation_ifUser_thenOk() {
        mockMvc.perform(post("/users/requests?eventId=1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createParticipation_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/users/requests?eventId=1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void getParticipation_ifUser_thenOk() {
        mockMvc.perform(get("/users/requests")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getParticipation_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/users/requests"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void cancelParticipation_ifUser_thenOk() {
        mockMvc.perform(patch("/users/requests/1/cancel")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void cancelParticipation_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/users/requests/1/cancel"))
                .andExpect(status().isForbidden());
    }
}