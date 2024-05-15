package ru.practicum.explorewithme.basic.service.users.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.explorewithme.basic.service.users.models.QUser;
import ru.practicum.explorewithme.basic.service.users.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAllWithOffsetAndLimit(BooleanExpression filter, int from, int size) {
        QUser user = QUser.user;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(user)
                .where(filter)
                .orderBy(user.id.asc())
                .limit(size)
                .offset(from)
                .fetch();
    }
}
