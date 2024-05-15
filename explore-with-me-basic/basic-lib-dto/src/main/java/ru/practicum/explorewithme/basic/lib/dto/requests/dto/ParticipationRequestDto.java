package ru.practicum.explorewithme.basic.lib.dto.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.explorewithme.basic.lib.dto.requests.enums.ParticipationState;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Getter
public class ParticipationRequestDto {
    private final Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private final LocalDateTime created;

    private final Long event;
    private final Long requester;
    private final ParticipationState status;
}
