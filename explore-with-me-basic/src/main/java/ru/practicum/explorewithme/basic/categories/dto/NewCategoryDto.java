package ru.practicum.explorewithme.basic.categories.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Jacksonized
public class NewCategoryDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private final String name;
}
