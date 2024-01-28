package ru.practicum.explorewithme.basic.events.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.events.enums.EventState;
import ru.practicum.explorewithme.basic.events.models.Event;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom, QuerydslPredicateExecutor<Event> {

    @EntityGraph(value = "event-fetch-all")
    Optional<Event> findByIdAndInitiator_Id(long eventId, long userId);

    @NonNull
    @EntityGraph(value = "event-fetch-all")
    Optional<Event> findEventById(@NonNull Long id);

    @EntityGraph(value = "event-fetch-all")
    Optional<Event> findByIdAndState(long eventId, EventState published);

}
