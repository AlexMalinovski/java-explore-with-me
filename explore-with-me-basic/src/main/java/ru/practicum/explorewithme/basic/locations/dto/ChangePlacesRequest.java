package ru.practicum.explorewithme.basic.locations.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Validated
@Getter
@Jacksonized
public class ChangePlacesRequest {

    @NotNull
    private final Set<Long> placeIds;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private final PlaceStatus newStatus;
}
