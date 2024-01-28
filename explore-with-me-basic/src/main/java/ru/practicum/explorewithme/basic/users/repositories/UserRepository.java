package ru.practicum.explorewithme.basic.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.basic.users.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
