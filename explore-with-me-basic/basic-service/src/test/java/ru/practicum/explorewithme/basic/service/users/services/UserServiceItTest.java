package ru.practicum.explorewithme.basic.service.users.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.service.users.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceItTest {

    @Autowired
    private UserService userService;

    @Test
    @Sql("/delete-all.sql")
    void createUser() {
        var actual = userService.createUser(User.builder()
                .name("name")
                .email("user@mail.ru")
                .build());

        assertNotNull(actual);
        assertEquals("name", actual.getName());
        assertEquals("user@mail.ru", actual.getEmail());
        assertEquals(1L, actual.getId());
    }
}