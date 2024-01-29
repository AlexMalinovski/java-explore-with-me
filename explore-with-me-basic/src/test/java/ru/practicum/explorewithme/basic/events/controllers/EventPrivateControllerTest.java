package ru.practicum.explorewithme.basic.events.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.events.dto.NewEventDto;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.events.mappers.EventMapper;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.events.services.EventService;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.requests.mappers.ParticipationMapper;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventPrivateController.class)
class EventPrivateControllerTest {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @MockBean
    private EventService eventService;

    @MockBean
    private EventMapper eventMapper;

    @MockBean
    private ParticipationMapper participationMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private NewEventDto getNewEventDto() {
        return NewEventDto.builder()
                .annotation("a".repeat(20))
                .category(1L)
                .description("d".repeat(20))
                .eventDate("2080-01-01 00:00:00")
                .location(LocationDto.builder()
                        .lat(new BigDecimal("50.02"))
                        .lon(new BigDecimal("80.05"))
                        .build())
                .title("t".repeat(3)).build();
    }

    @Test
    @SneakyThrows
    void createEvent_isAvailable() {
        NewEventDto body = getNewEventDto();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(eventService).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidAnnotation_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .annotation(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .annotation("")
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .annotation(" ".repeat(50))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .annotation("a".repeat(19))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .annotation("a".repeat(2001))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidCategory_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .category(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .category(-1L)
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .category(0L)
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidDescription_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .description(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .description("")
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .description(" ".repeat(50))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .description("a".repeat(19))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .description("a".repeat(7001))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidEventDate_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .eventDate(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(Event.builder().build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .eventDate("")
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .eventDate("2080-01-01T00:00:00")
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());


        LocalDateTime invalidDate = LocalDateTime.now().plusMinutes(120);
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(Event.builder().eventDate(invalidDate).build());
        body = getNewEventDto().toBuilder()
                .eventDate(invalidDate.format(dateFormatter))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidLocation_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .location(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());
        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    @SneakyThrows
    void createEvent_ifInvalidTitle_thenBadRequest() {
        NewEventDto body = getNewEventDto().toBuilder()
                .title(null)
                .build();
        when(eventMapper.mapToEvent(any(NewEventDto.class))).thenReturn(
                Event.builder()
                        .eventDate(LocalDateTime.parse(body.getEventDate(), dateFormatter))
                        .build());
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .title("")
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .title(" ".repeat(20))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .title("a".repeat(2))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = getNewEventDto().toBuilder()
                .title("a".repeat(122))
                .build();
        mockMvc.perform(post("/users/10/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).createEvent(any(Event.class));
    }


    @Test
    @SneakyThrows
    void getEvents_isAvailable() {
        mockMvc.perform(get("/users/1/events")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(eventService).getEvents(1, 0, 10);
    }

    @Test
    @SneakyThrows
    void getEvents_withDefaultParameters_isAvailable() {
        mockMvc.perform(get("/users/1/events"))
                .andExpect(status().isOk());

        verify(eventService).getEvents(1, 0, 10);
    }

    @Test
    @SneakyThrows
    void getEvents_ifInvalidFrom_thenBadRequest() {
        mockMvc.perform(get("/users/1/events")
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEvents(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEvents_ifInvalidSize_thenBadRequest() {
        mockMvc.perform(get("/users/1/events")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/users/1/events")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).getEvents(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getEventById_isAvailable() {
        mockMvc.perform(get("/users/1/events/2"))
                .andExpect(status().isOk());

        verify(eventService).getEventById(1, 2);
    }

    @Test
    @SneakyThrows
    void updateEvent_isAvailable() {
        UpdateEventUserRequest body = UpdateEventUserRequest.builder().build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(eventService).updateEvent(eq(1L), eq(2L), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidAnnotation_thenBadRequest() {
        UpdateEventUserRequest body = UpdateEventUserRequest.builder().annotation("").build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().annotation(" ".repeat(30)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().annotation("a".repeat(19)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().annotation("a".repeat(2001)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), anyLong(), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidDescription_thenBadRequest() {
        UpdateEventUserRequest body = UpdateEventUserRequest.builder().description("").build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().description(" ".repeat(30)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().description("a".repeat(19)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().description("a".repeat(7001)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), anyLong(), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidEventDate_thenBadRequest() {
        UpdateEventUserRequest body = UpdateEventUserRequest.builder().eventDate(LocalDateTime.now().plusHours(2)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().eventDate(LocalDateTime.now()).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().eventDate(LocalDateTime.now().minusHours(1)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), anyLong(), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidStateAction_thenBadRequest() {
        for (StateAction action : StateAction.values()) {
            if (action == StateAction.SEND_TO_REVIEW || action == StateAction.CANCEL_REVIEW) {
                continue;
            }
            UpdateEventUserRequest body = UpdateEventUserRequest.builder().stateAction(action).build();
            mockMvc.perform(patch("/users/1/events/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(body)))
                    .andExpect(status().isBadRequest());
        }
        verify(eventService, never()).updateEvent(anyLong(), anyLong(), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEvent_ifInvalidTitle_thenBadRequest() {
        UpdateEventUserRequest body = UpdateEventUserRequest.builder().title("").build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().title(" ".repeat(30)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().title("a".repeat(2)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = UpdateEventUserRequest.builder().title("a".repeat(121)).build();
        mockMvc.perform(patch("/users/1/events/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEvent(anyLong(), anyLong(), any(UpdateEventUserRequest.class));
    }

    @Test
    @SneakyThrows
    void getEventParticipation_isAvailable() {
        mockMvc.perform(get("/users/1/events/2/requests"))
                .andExpect(status().isOk());

        verify(eventService).getEventParticipation(1, 2);
    }

    @Test
    @SneakyThrows
    void updateEventState_isAvailable() {
        EventRequestStatusUpdateRequest body = EventRequestStatusUpdateRequest.builder().requestIds(List.of(1L)).status(ParticipationState.CONFIRMED).build();
        mockMvc.perform(patch("/users/1/events/2/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isOk());

        verify(eventService).updateEventState(eq(1L), eq(2L), any(EventRequestStatusUpdateRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEventState_ifInvalidRequestIds_thenBadRequest() {
        EventRequestStatusUpdateRequest body = EventRequestStatusUpdateRequest.builder().status(ParticipationState.CONFIRMED).build();
        mockMvc.perform(patch("/users/1/events/2/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(eventService, never()).updateEventState(anyLong(), anyLong(), any(EventRequestStatusUpdateRequest.class));
    }

    @Test
    @SneakyThrows
    void updateEventState_ifInvalidStatus_thenBadRequest() {
        for (ParticipationState status : ParticipationState.values()) {
            if (status == ParticipationState.CONFIRMED || status == ParticipationState.REJECTED) {
                continue;
            }
            EventRequestStatusUpdateRequest body = EventRequestStatusUpdateRequest.builder().requestIds(List.of(1L)).status(status).build();
            mockMvc.perform(patch("/users/1/events/2/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(body)))
                    .andExpect(status().isBadRequest());
        }

        verify(eventService, never()).updateEventState(anyLong(), anyLong(), any(EventRequestStatusUpdateRequest.class));
    }
}