package ru.practicum.explorewithme.basic.lib.dto.compilations;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Getter
public class NewCompilationDto {

    private final Set<Long> events;
    private final boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    private final String title;
}
