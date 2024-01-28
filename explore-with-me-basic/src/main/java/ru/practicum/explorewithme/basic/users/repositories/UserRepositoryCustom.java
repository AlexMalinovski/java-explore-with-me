package ru.practicum.explorewithme.basic.users.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.explorewithme.basic.users.models.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> findAllWithOffsetAndLimit(BooleanExpression filter, int from, int size);
}
