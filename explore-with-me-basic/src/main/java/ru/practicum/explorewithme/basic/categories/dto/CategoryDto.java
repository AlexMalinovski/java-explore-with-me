package ru.practicum.explorewithme.basic.categories.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder(toBuilder = true)
@Getter
public class CategoryDto {
    private final Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private final String name;
}
