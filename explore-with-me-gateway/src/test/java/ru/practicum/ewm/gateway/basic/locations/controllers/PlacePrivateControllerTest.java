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
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.NewPlaceDto;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlacePrivateController.class)
@Import(SecurityConfig.class)
class PlacePrivateControllerTest {

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
    void createPlace_ifUser_thenOk() {
        mockMvc.perform(post("/users/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewPlaceDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createPlace_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/users/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewPlaceDto())))
                .andExpect(status().isForbidden());
    }
}