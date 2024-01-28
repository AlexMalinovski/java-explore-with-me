package ru.practicum.explorewithme.basic.users.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserShortDto {
    private final long id;
    private final String name;
}
