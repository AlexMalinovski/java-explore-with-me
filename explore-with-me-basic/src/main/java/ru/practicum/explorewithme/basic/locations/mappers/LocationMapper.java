package ru.practicum.explorewithme.basic.locations.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.locations.models.embeddable.Location;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Mapper(componentModel = "spring",
        imports = {BigDecimal.class, RoundingMode.class, Double.class})
public interface LocationMapper {

    @Mapping(target = "lat", expression = "java((new BigDecimal(Double.toString(location.getLat()))).setScale(6, RoundingMode.DOWN))")
    @Mapping(target = "lon", expression = "java((new BigDecimal(Double.toString(location.getLon()))).setScale(6, RoundingMode.DOWN))")
    LocationDto mapToLocationDto(Location location);

    Location mapToLocation(LocationDto dto);
}
