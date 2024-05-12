package ru.practicum.explorewithme.basic.service.compilations.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.explorewithme.basic.lib.dto.compilations.UpdateCompilationRequest;
import ru.practicum.explorewithme.basic.service.compilations.models.Compilation;

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
