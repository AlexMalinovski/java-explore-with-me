package ru.practicum.explorewithme.basic.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Jacksonized
public class NewUserDto {

    @NotBlank
    @Size(min = 2, max = 250)
    private final String name;

    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private final String email;
}
