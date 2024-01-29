package ru.practicum.explorewithme.basic.events.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Validated
@Getter
@Jacksonized
public class NewEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private final String annotation;

    @NotNull
    @Positive
    @Max(Long.MAX_VALUE)
    private final Long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private final String description;

    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "In format: yyyy-MM-dd HH:mm:ss")
    private final String eventDate; // "yyyy-MM-dd HH:mm:ss"

    @NotNull
    @Valid
    private final LocationDto location;

    private final boolean paid;

    private final int participantLimit;

    @Builder.Default
    private final boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private final String title;

}
