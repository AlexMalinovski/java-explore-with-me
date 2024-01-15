package ru.practicum.explorewithme.statistics.lib.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@EqualsAndHashCode
public class CreateEndpointHitDto {

    @NotBlank
    @Size(min = 1, max = 32)
    private final String app;

    @NotBlank
    @Size(min = 1, max = 256)
    private final String uri;

    @NotBlank
    @Size(min = 1, max = 15)
    private final String ip;

    @NotBlank
    private final String timestamp;
}
