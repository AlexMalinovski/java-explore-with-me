package ru.practicum.explorewithme.basic.service.requests.services;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;

import java.util.List;

public interface ParticipationService {

    @NonNull
    Participation createParticipation(long userId, long eventId);

    @NonNull
    List<Participation> getUserParticipation(long userId);

    @NonNull
    Participation cancelParticipation(long userId, long requestId);
}
