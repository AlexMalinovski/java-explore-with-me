package ru.practicum.explorewithme.basic.compilations.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.compilations.models.QCompilation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CompilationRepositoryCustomImpl implements CompilationRepositoryCustom {

    @PersistenceContext
    private EntityManager em;


    @Override
    public List<Compilation> findByWithOffsetAndLimitFetch(BooleanExpression filter, int from, int size) {
        QCompilation compilation = QCompilation.compilation;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory
                .selectFrom(compilation)
                .leftJoin(compilation.events).fetchJoin()
                .where(filter)
                .orderBy(compilation.id.asc())
                .limit(size)
                .offset(from)
                .fetch();
    }
}
