package ru.practicum.explorewithme.basic.service.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.service.users.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
