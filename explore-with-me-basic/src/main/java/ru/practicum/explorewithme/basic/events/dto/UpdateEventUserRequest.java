package ru.practicum.explorewithme.basic.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.practicum.explorewithme.basic.events.enums.StateAction;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@Validated
@Getter
@Jacksonized
public class UpdateEventUserRequest {

    @Pattern(regexp = "^\\S+.*$", message = "Not blank string", flags = Pattern.Flag.DOTALL)
    @Size(min = 20, max = 2000)
    private final String annotation;

    @Pattern(regexp = "^\\S+.*$", message = "Not blank string", flags = Pattern.Flag.DOTALL)
    @Size(min = 20, max = 7000)
    private final String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime eventDate;

    @Valid
    private final LocationDto location;

    private final Boolean paid;

    private final Integer participantLimit;

    private final Boolean requestModeration;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private final StateAction stateAction; // [ SEND_TO_REVIEW, CANCEL_REVIEW ]

    @Pattern(regexp = "^\\S+.*$", message = "Not blank string")
    @Size(min = 3, max = 120)
    private final String title;

}
