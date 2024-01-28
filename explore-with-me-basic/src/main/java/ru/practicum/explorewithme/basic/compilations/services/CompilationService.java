package ru.practicum.explorewithme.basic.compilations.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.basic.compilations.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.compilations.models.Compilation;

import java.util.List;

public interface CompilationService {

    @NonNull
    Compilation createCompilation(@NonNull Compilation compilation);

    void deleteCompilation(long compId);

    @NonNull
    Compilation updateCompilation(long compId, @NonNull UpdateCompilationRequest request);

    @NonNull
    List<Compilation> getCompilationsPublic(@Nullable Boolean pinned, int from, int size);

    @NonNull
    Compilation getCompilationById(long compId);
}
