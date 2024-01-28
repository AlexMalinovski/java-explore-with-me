package ru.practicum.explorewithme.basic.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.explorewithme.basic.requests.enums.ParticipationState;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Jacksonized
public class EventRequestStatusUpdateRequest {

    @NotNull
    private final List<Long> requestIds;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private final ParticipationState status; //(CONFIRMED|REJECTED)
}
