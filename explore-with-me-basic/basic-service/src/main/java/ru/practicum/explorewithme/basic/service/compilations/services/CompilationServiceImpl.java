package ru.practicum.explorewithme.basic.service.compilations.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.service.compilations.mappers.CompilationMapper;
import ru.practicum.explorewithme.basic.service.compilations.models.Compilation;
import ru.practicum.explorewithme.basic.service.compilations.models.QCompilation;
import ru.practicum.explorewithme.basic.service.compilations.repositories.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final String notFoundMsg = "Compilation with id=%d was not found";
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    @NonNull
    public Compilation createCompilation(@NonNull Compilation compilation) {
        if (compilation.getId() != null) {
            throw new IllegalArgumentException("Id can be null.");
        }
        return compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(long compId) {
        if (compId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, compId));
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    @NonNull
    @Transactional
    public Compilation updateCompilation(long compId, @NonNull UpdateCompilationRequest request) {
        if (compId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, compId));
        }
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(notFoundMsg, compId)));
        Compilation updates = compilationMapper.updateCompilation(compilation, request);
        compilationRepository.save(updates);
        return compilationRepository.findCompilationById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(notFoundMsg, compId)));
    }

    @Override
    @NonNull
    public List<Compilation> getCompilationsPublic(@Nullable Boolean pinned, int from, int size) {
        BooleanExpression filter = Expressions.TRUE.isTrue();
        if (pinned != null) {
            filter = filter.and(QCompilation.compilation.pinned.eq(pinned));
        }

        return compilationRepository.findByWithOffsetAndLimitFetch(filter, from, size);
    }

    @Override
    @NonNull
    public Compilation getCompilationById(long compId) {
        if (compId <= 0) {
            throw new NotFoundException(String.format(notFoundMsg, compId));
        }

        return compilationRepository.findCompilationById(compId)
                .orElseThrow(() -> new NotFoundException(String.format(notFoundMsg, compId)));
    }
}
