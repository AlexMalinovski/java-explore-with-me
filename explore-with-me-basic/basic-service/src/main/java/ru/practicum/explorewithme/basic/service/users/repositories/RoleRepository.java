package ru.practicum.explorewithme.basic.service.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.service.users.models.Role;


import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findOneByName(String name);

    Set<Role> findByNameIn(Collection<String> names);
}
