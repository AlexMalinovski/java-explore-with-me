package ru.practicum.explorewithme.basic.service.compilations.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.service.compilations.models.Compilation;

import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>, CompilationRepositoryCustom {

    @EntityGraph("compilation-fetch-events")
    Optional<Compilation> findCompilationById(long compId);

}
