package ru.practicum.explorewithme.basic.service.requests.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ParticipationServiceItTest {

    @Autowired
    private ParticipationService participationService;

    @Test
    @Sql("/participation-service-it-test.sql")
    void crud() {
        Participation created = participationService.createParticipation(2L, 1L);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(ParticipationState.CONFIRMED, created.getStatus());
        assertEquals(2L, created.getRequester().getId());
        assertEquals(1L, created.getEvent().getId());

        List<Participation> read = participationService.getUserParticipation(2L);
        assertNotNull(read);
        assertEquals(1, read.size());
        assertEquals(created.getRequester().getId(), read.get(0).getRequester().getId());
        assertEquals(created.getEvent().getId(), read.get(0).getEvent().getId());
        assertEquals(created.getStatus(), read.get(0).getStatus());
        assertEquals(created.getId(), read.get(0).getId());

        Participation cancelled = participationService.cancelParticipation(2L, created.getId());
        assertNotNull(cancelled);
        assertEquals(created.getRequester().getId(), cancelled.getRequester().getId());
        assertEquals(created.getEvent().getId(), cancelled.getEvent().getId());
        assertEquals(ParticipationState.CANCELED, cancelled.getStatus());
        assertEquals(created.getId(), read.get(0).getId());
    }

}