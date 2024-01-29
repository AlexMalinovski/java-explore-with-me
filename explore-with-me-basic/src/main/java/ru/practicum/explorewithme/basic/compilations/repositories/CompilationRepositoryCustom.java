package ru.practicum.explorewithme.basic.compilations.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;

import java.util.List;

public interface CompilationRepositoryCustom {
    List<Compilation> findByWithOffsetAndLimitFetch(BooleanExpression filter, int from, int size);

}
