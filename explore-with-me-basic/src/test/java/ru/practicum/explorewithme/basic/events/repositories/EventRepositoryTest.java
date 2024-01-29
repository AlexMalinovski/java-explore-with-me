package ru.practicum.explorewithme.basic.events.repositories;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.events.enums.EventState;
import ru.practicum.explorewithme.basic.events.models.QEvent;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    @Sql("/event-repository-it-test.sql")
    void findByWithOffsetAndLimitFetch() {
        BooleanExpression filter = QEvent.event.state.eq(EventState.PUBLISHED);

        var actual = eventRepository.findByWithOffsetAndLimitFetch(filter, 1, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3L, actual.get(0).getId());

    }

    @Test
    @Sql("/event-repository-it-test.sql")
    void findByWithOffsetAndLimitFetch_order() {

        BooleanExpression filter = QEvent.event.state.eq(EventState.PUBLISHED);
        OrderSpecifier<LocalDateTime> order = QEvent.event.eventDate.desc();

        var actual = eventRepository.findByWithOffsetAndLimitFetch(filter, order, 0, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(4L, actual.get(0).getId());
    }

}