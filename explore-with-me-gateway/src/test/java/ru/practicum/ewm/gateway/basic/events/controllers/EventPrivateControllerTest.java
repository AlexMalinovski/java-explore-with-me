package ru.practicum.ewm.gateway.basic.events.controllers;

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
import ru.practicum.explorewithme.basic.lib.dto.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.lib.dto.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.lib.dto.requests.dto.EventRequestStatusUpdateRequest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EventPrivateController.class)
@Import(SecurityConfig.class)
class EventPrivateControllerTest {

    @MockBean
    private RestTemplate basicServiceRestTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NewEventDto getValidNewEventDto() {
        return NewEventDto.builder()
                .annotation("jshdvsdlkjsjvblksjdbvlkjsdfdsf")
                .category(1L)
                .description("hjdfvbkjhsdbfvjhbsdfjvhbskjdhfvbks")
                .eventDate("2100-01-01 00:00:01")
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.0"))
                        .lon(new BigDecimal("50.0"))
                        .build())
                .title("title")
                .build();
    }

    private EventRequestStatusUpdateRequest getValidEventRequestStatusUpdateRequest() {
        return EventRequestStatusUpdateRequest.builder().requestIds(Collections.emptyList()).build();
    }

    @Test
    @SneakyThrows
    void createEvent_ifUser_thenOk() {
        mockMvc.perform(post("/users/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewEventDto()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void createEvent_ifAnonymous_thenForbidden() {
        mockMvc.perform(post("/users/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidNewEventDto())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void getEvents_ifUser_thenOk() {
        mockMvc.perform(get("/users/events")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getEvents_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/users/events"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void getEventById_ifUser_thenOk() {
        mockMvc.perform(get("/users/events/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void getEventById_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/users/events/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void updateEvent_ifUser_thenOk() {
        mockMvc.perform(patch("/users/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdateEventUserRequest.builder().build()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void updateEvent_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/users/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UpdateEventUserRequest.builder().build())))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void getEventParticipation_ifUser_thenOk() {
        mockMvc.perform(get("/users/events/1/requests")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void getEventParticipation_ifAnonymous_thenUnauthorized() {
        mockMvc.perform(get("/users/events/1/requests"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void updateEventState_ifUser_thenOk() {
        mockMvc.perform(patch("/users/events/1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidEventRequestStatusUpdateRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void updateEventState_ifAnonymous_thenForbidden() {
        mockMvc.perform(patch("/users/events/1/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidEventRequestStatusUpdateRequest())))
                .andExpect(status().isForbidden());
    }
}