package ru.practicum.explorewithme.basic.events.services;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.basic.events.dto.GetEventRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.basic.events.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.basic.events.models.Event;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.basic.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.basic.requests.models.Participation;

import java.util.List;

public interface EventService {

    @NonNull
    Event createEvent(@NonNull Event event);

    @NonNull
    List<Event> getEvents(long userId, int from, int size);

    @NonNull
    Event getEventById(long userId, long eventId);

    @NonNull
    Event getEventById(long eventId);

    @NonNull
    Event updateEvent(long userId, long eventId, @NonNull UpdateEventUserRequest request);

    @NonNull
    Event updateEvent(long eventId, @NonNull UpdateEventAdminRequest request);

    @NonNull
    List<Event> getEventsAdmin(@NonNull GetEventRequest getEventRequest, int from, int size);

    @NonNull
    Event getPublishEventById(long eventId);

    @NonNull
    List<Event> getEventsPublic(@NonNull GetEventRequest getEventRequest, int from, int size);

    @NonNull
    List<Participation> getEventParticipation(long userId, long eventId);

    @NonNull
    EventRequestStatusUpdateResult updateEventState(long userId, long eventId, @NonNull EventRequestStatusUpdateRequest request);
}
