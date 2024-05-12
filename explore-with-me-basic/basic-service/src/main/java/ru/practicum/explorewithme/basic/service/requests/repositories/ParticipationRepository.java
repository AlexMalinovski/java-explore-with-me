package ru.practicum.explorewithme.basic.service.requests.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.service.requests.models.Participation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long>, QuerydslPredicateExecutor<Participation> {
    boolean existsByEvent_IdAndRequester_Id(long eventId, long userId);

    List<Participation> findAllByRequester_Id(long userId);

    @EntityGraph(value = "participation-fetch-event")
    Optional<Participation> findByIdAndRequester_Id(long requestId, long userId);
}
