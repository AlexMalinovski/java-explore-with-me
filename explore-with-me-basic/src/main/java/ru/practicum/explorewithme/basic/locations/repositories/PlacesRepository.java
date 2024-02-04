package ru.practicum.explorewithme.basic.locations.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.locations.models.Place;

@Repository
public interface PlacesRepository extends JpaRepository<Place, Long>, QuerydslPredicateExecutor<Place>, PlacesRepositoryCustom {
}
