package ru.practicum.explorewithme.basic.lib.dto.compilations;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Getter
public class UpdateCompilationRequest {
    private final Set<Long> events;
    private final Boolean pinned;

    @Pattern(regexp = "^\\S+.*$", message = "Not blank string")
    @Size(min = 1, max = 50)
    private final String title;
}
