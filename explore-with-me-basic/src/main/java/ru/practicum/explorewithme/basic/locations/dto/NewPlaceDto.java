package ru.practicum.explorewithme.basic.locations.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Validated
@Getter
@Jacksonized
public class NewPlaceDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private final String name;

    @NotBlank
    @Size(min = 20, max = 7000)
    private final String description;

    @NotNull
    @Valid
    private final LocationDto location;

    @NotNull
    @Positive
    private final Integer radius;
}
