package ru.practicum.explorewithme.basic.lib.dto.requests.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class EventRequestStatusUpdateResult {

    @Builder.Default()
    private final List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();

    @Builder.Default()
    private final List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
