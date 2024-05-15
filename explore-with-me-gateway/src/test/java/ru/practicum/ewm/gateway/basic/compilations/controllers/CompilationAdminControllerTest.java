package ru.practicum.ewm.gateway.basic.compilations.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.gateway.configs.SecurityConfig;
import ru.practicum.explorewithme.basic.lib.dto.compilations.NewCompilationDto;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompilationAdminController.class)
@Import(SecurityConfig.class)
class CompilationAdminControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createCompilation_ifAdmin_thenOk() {
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(NewCompilationDto.builder().title("name").build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createCompilation_ifUser_thenForbidden() {
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(NewCompilationDto.builder().title("name").build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createCompilation_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(NewCompilationDto.builder().title("name").build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void deleteCompilation_ifAdmin_thenNoContent() {
        mockMvc.perform(delete("/admin/compilations/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void deleteCompilation_ifUser_thenForbidden() {
        mockMvc.perform(delete("/admin/compilations/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void deleteCompilation_ifAnonymous_thenForbidden() {
        mockMvc.perform(delete("/admin/compilations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updateCompilation_ifAdmin_thenOk() {
        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdateCompilationRequest.builder().build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updateCompilation_ifUser_thenForbidden() {
        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdateCompilationRequest.builder().build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void updateCompilation_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/admin/compilations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdateCompilationRequest.builder().build())))
                .andExpect(status().isForbidden());
    }
}