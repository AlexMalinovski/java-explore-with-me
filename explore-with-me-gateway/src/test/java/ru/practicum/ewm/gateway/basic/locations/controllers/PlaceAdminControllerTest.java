package ru.practicum.ewm.gateway.basic.locations.controllers;

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
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.UpdatePlaceAdminRequest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlaceAdminController.class)
@Import(SecurityConfig.class)
class PlaceAdminControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NewPlaceDto getValidNewPlaceDto() {
        return NewPlaceDto.builder()
                .name("name")
                .description("dkfjhvbsdvbsjdfvsfdvsdfhvdssd vkhsd")
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.0"))
                        .lon(new BigDecimal("50.0"))
                        .build())
                .radius(1000)
                .build();
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifAdmin_thenOk() {
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewPlaceDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void createAndPublishPlace_ifUser_thenForbidden() {
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewPlaceDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createAndPublishPlace_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewPlaceDto())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updatePlaces_ifAdmin_thenOk() {
        mockMvc.perform(patch("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ChangePlacesRequest.builder().placeIds(Collections.emptySet()).build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updatePlaces_ifUser_thenForbidden() {
        mockMvc.perform(patch("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ChangePlacesRequest.builder().placeIds(Collections.emptySet()).build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void updatePlaces_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/admin/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ChangePlacesRequest.builder().placeIds(Collections.emptySet()).build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void updatePlaceById_ifAdmin_thenOk() {
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdatePlaceAdminRequest.builder().build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void updatePlaceById_ifUser_thenForbidden() {
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdatePlaceAdminRequest.builder().build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void updatePlaceById_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/admin/places/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdatePlaceAdminRequest.builder().build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void deletePlace_ifAdmin_thenNoContent() {
        mockMvc.perform(delete("/admin/places/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    void deletePlace_ifUser_thenForbidden() {
        mockMvc.perform(delete("/admin/places/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void deletePlace_ifAnonymous_thenForbidden() {
        mockMvc.perform(delete("/admin/places/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void getPlaceById_ifAdmin_thenOk() {
        mockMvc.perform(get("/admin/places/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getPlaceById_ifUser_thenForbidden() {
        mockMvc.perform(get("/admin/places/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getPlaceById_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/admin/places/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void getPlaces_ifAdmin_thenOk() {
        mockMvc.perform(get("/admin/places")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getPlaces_ifUser_thenForbidden() {
        mockMvc.perform(get("/admin/places")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getPlaces_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/admin/places"))
                .andExpect(status().isUnauthorized());
    }
}