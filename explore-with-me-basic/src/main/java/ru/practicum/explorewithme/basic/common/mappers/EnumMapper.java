package ru.practicum.explorewithme.basic.common.mappers;

import ru.practicum.exceptions.api.BadRequestException;

public class EnumMapper {
    public <T extends Enum<T>> T toEnum(String src, Class<T> enumType) {
        if (src == null) {
            return null;
        }
        try {
            return T.valueOf(enumType, src.toUpperCase());
        } catch (Exception ex) {
            throw new BadRequestException(String.format(ex.getMessage()));
        }
    }
}
