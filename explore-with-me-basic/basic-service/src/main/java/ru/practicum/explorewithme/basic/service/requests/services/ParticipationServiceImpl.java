package ru.practicum.explorewithme.basic.service.requests.services;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.api.ForbiddenException;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.lib.dto.events.enums.EventState;
import ru.practicum.explorewithme.basic.service.events.models.Event;
import ru.practicum.explorewithme.basic.service.events.repositories.EventRepository;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;
import ru.practicum.explorewithme.basic.service.requests.repositories.ParticipationRepository;
import ru.practicum.explorewithme.basic.service.users.models.User;
import ru.practicum.explorewithme.basic.service.users.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @NonNull
    @Transactional
    public Participation createParticipation(long userId, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found.", eventId)));
        if (event.getInitiator().getId() == userId) {
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии.");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии.");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ForbiddenException("Достигнут лимит запросов на участие.");
        }

        Participation.ParticipationBuilder builder = Participation
                .builder()
                .event(Event.builder().id(eventId).build())
                .requester(User.builder().id(userId).build());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            builder.status(ParticipationState.CONFIRMED);
            Event updatedEvent = event.toBuilder()
                    .confirmedRequests(event.getConfirmedRequests() + 1)
                    .build();
            eventRepository.save(updatedEvent);
        } else {
            builder.status(ParticipationState.PENDING);
        }
        return participationRepository.save(builder.build());
    }

    @Override
    @NonNull
    public List<Participation> getUserParticipation(long userId) {
        if (userId <= 0 || !userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%d was not found.", userId));
        }
        return participationRepository.findAllByRequester_Id(userId);
    }

    @Override
    @NonNull
    @Transactional
    public Participation cancelParticipation(long userId, long requestId) {
        Participation participation = participationRepository.findByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        if (participation.getStatus() == ParticipationState.CONFIRMED) {
            Event updatedEvent = participation.getEvent().toBuilder()
                    .confirmedRequests(participation.getEvent().getConfirmedRequests() - 1)
                    .build();
            eventRepository.save(updatedEvent);
        }
        return participationRepository.save(
                participation.toBuilder()
                        .status(ParticipationState.CANCELED)
                        .build());
    }
}
