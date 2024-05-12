package ru.practicum.explorewithme.basic.service.users.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.service.users.models.QUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Sql("/user-repository-it-test.sql")
    void findAllWithOffsetAndLimit() {
        BooleanExpression filter = QUser.user.id.goe(2);

        var actual = userRepository.findAllWithOffsetAndLimit(filter, 1, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3L, actual.get(0).getId());
    }

}