package ru.practicum.explorewithme.basic.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.basic.users.dto.NewUserDto;
import ru.practicum.explorewithme.basic.users.mappers.UserMapper;
import ru.practicum.explorewithme.basic.users.services.UserService;
import ru.practicum.explorewithme.statistics.client.StatsClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAdminController.class)
class UserAdminControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private StatsClient statsClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void getUsers_isAvailable() {
        mockMvc.perform(get("/admin/users")
                        .param("ids", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(userService).getUsers(List.of(1L), 0, 10);
    }

    @Test
    @SneakyThrows
    void getUsers_defaultParam() {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());

        verify(userService).getUsers(null, 0, 10);
    }

    @Test
    @SneakyThrows
    void getUsers_ifInvalidFrom_thenBadRequest() {
        mockMvc.perform(get("/admin/users")
                        .param("from", "-1"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUsers(anyList(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getUsers_ifInvalidSize_thenBadRequest() {
        mockMvc.perform(get("/admin/users")
                        .param("size", "-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/admin/users")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUsers(anyList(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void createUser_isAvailable() {
        NewUserDto body = NewUserDto.builder().name("title").email("e@mail.ru").build();

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isCreated());

        verify(userService).createUser(any());
    }

    @Test
    @SneakyThrows
    void createUser_ifInvalidName_thenBadRequest() {
        NewUserDto body = NewUserDto.builder()
                .email("e@mail.ru")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("")
                .email("e@mail.ru")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("a")
                .email("e@mail.ru")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("a".repeat(251))
                .email("e@mail.ru")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @SneakyThrows
    void createUser_ifInvalidEmail_thenBadRequest() {
        NewUserDto body = NewUserDto.builder()
                .name("name")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("name")
                .email("")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("name")
                .email("email")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("name")
                .email("ema.il")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("name")
                .email("e@m.l")
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        body = NewUserDto.builder()
                .name("name")
                .email("e@m." + "l".repeat(251))
                .build();
        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(body)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any());
    }

    @Test
    @SneakyThrows
    void deleteUser_isAvailable() {
        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}