package ru.practicum.ewm.gateway.basic.users.controllers;

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
import ru.practicum.explorewithme.basic.lib.dto.users.NewUserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserAdminController.class)
@Import(SecurityConfig.class)
class UserAdminControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NewUserDto getValidNewUserDto() {
        return NewUserDto.builder().name("name").email("e@mail.ef").build();
    }


    @Test
    @SneakyThrows
    void getUsers_ifAdmin_thenOk() {
        mockMvc.perform(get("/admin/users")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getUsers_ifUser_thenForbidden() {
        mockMvc.perform(get("/admin/users")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getUsers_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void createUser_ifAdmin_thenOk() {
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewUserDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createUser_ifUser_thenForbidden() {
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewUserDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createUser_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewUserDto())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void deleteUser_ifAdmin_thenNoContent() {
        mockMvc.perform(delete("/admin/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void deleteUser_ifUser_thenForbidden() {
        mockMvc.perform(delete("/admin/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void deleteUser_ifAnonymous_thenForbidden() {
        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isForbidden());
    }
}