package ru.practicum.explorewithme.basic.lib.dto.users;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private final long id;
    private final String name;
    private final String email;
}
