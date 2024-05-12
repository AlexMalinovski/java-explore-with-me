package ru.practicum.explorewithme.basic.lib.dto.locations.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Validated
@Getter
@Jacksonized
public class UpdatePlaceAdminRequest {

    @Size(min = 1, max = 50)
    @Pattern(regexp = "^\\S+.*$", message = "Not blank string")
    private final String name;

    @Size(min = 20, max = 7000)
    @Pattern(regexp = "^\\S+.*$", message = "Not blank string", flags = Pattern.Flag.DOTALL)
    private final String description;

    @Valid
    private final LocationDto location;

    @Positive
    private final Integer radius;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private final PlaceStatus status;
}
