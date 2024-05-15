package ru.practicum.explorewithme.basic.service.compilations.repositories;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.explorewithme.basic.service.compilations.models.QCompilation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CompilationRepositoryTest {

    @Autowired
    private CompilationRepository compilationRepository;

    @Test
    @Sql("/compilation-repository-it-test.sql")
    void findByWithOffsetAndLimitFetch() {
        BooleanExpression filter = QCompilation.compilation.pinned.eq(false);

        var actual = compilationRepository.findByWithOffsetAndLimitFetch(filter, 1, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(4L, actual.get(0).getId());
    }

}