package ru.practicum.explorewithme.basic.users.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.explorewithme.basic.users.dto.NewUserDto;
import ru.practicum.explorewithme.basic.users.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    void mapToUser_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToUser(null);
        assertNull(actual);
    }

    @Test
    void mapToUser() {
        NewUserDto expected = NewUserDto.builder()
                .name("name")
                .email("e@mail.ru")
                .build();

        var actual = mapper.mapToUser(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertNull(actual.getId());
    }

    @Test
    void mapToUserDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToUserDto((User) null);
        assertNull(actual);
    }

    @Test
    void mapToUserDto() {
        User expected = User.builder()
                .id(1L)
                .name("name")
                .email("e@mail.ru")
                .build();

        var actual = mapper.mapToUserDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void mapToUserDto_List_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToUserDto((List<User>) null);
        assertNull(actual);
    }

    @Test
    void mapToUserDto_List() {
        List<User> expected = List.of(User.builder()
                .id(1L)
                .name("name")
                .email("e@mail.ru")
                .build());

        var actual = mapper.mapToUserDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getEmail(), actual.get(0).getEmail());
    }

    @Test
    void mapToUserShortDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapToUserShortDto(null);
        assertNull(actual);
    }

    @Test
    void mapToUserShortDto() {
        User expected = User.builder()
                .id(1L)
                .name("name")
                .email("e@mail.ru")
                .build();

        var actual = mapper.mapToUserShortDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}