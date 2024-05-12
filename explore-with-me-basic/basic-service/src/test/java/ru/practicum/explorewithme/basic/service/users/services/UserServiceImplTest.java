package ru.practicum.explorewithme.basic.service.users.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.basic.service.users.models.User;
import ru.practicum.explorewithme.basic.service.users.repositories.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getUsers() {
        List<User> expected = List.of();
        when(userRepository.findAllWithOffsetAndLimit(any(BooleanExpression.class), anyInt(), anyInt())).thenReturn(expected);

        var actual = userService.getUsers(List.of(), 0, 10);

        verify(userRepository).findAllWithOffsetAndLimit(any(BooleanExpression.class), eq(0), eq(10));
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void createUser_ifWithId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(User.builder().id(1L).build()));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser() {
        User expected = User.builder().build();
        when(userRepository.save(any())).thenReturn(expected);

        var actual = userService.createUser(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository).save(expected);
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}