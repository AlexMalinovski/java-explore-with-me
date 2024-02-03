package ru.practicum.explorewithme.basic.locations.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.basic.locations.dto.CircleSearchArea;
import ru.practicum.explorewithme.basic.locations.dto.LocationDto;
import ru.practicum.explorewithme.basic.locations.dto.NewPlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceDto;
import ru.practicum.explorewithme.basic.locations.dto.PlaceShortDto;
import ru.practicum.explorewithme.basic.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.models.embeddable.Location;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring",
        imports = {BigDecimal.class, RoundingMode.class, Double.class, PlaceStatus.class})
public interface LocationMapper {

    @Mapping(target = "lat", expression = "java(location.getLat() != null ? (new BigDecimal(Double.toString(location.getLat()))).setScale(6, RoundingMode.DOWN) : null)")
    @Mapping(target = "lon", expression = "java(location.getLon() != null ? (new BigDecimal(Double.toString(location.getLon()))).setScale(6, RoundingMode.DOWN) : null)")
    LocationDto mapToLocationDto(Location location);

    Location mapToLocation(LocationDto dto);

    @Mapping(target = "status", expression = "java(PlaceStatus.PENDING)")
    Place mapToPlace(NewPlaceDto dto);

    PlaceDto mapToPlaceDto(Place src);

    List<PlaceDto> mapToPlaceDto(List<Place> src);

    PlaceShortDto mapToPlaceShortDto(Place src);

    List<PlaceShortDto> mapToPlaceShortDto(List<Place> src);

    @Mapping(target = "name", expression = "java(upd != null && upd.getName() != null ? upd.getName() : src.getName())")
    @Mapping(target = "description", expression = "java(upd != null && upd.getDescription() != null ? upd.getDescription() : src.getDescription())")
    @Mapping(target = "location", expression = "java(upd != null && upd.getLocation() != null ? mapToLocation(upd.getLocation()) : src.getLocation())")
    @Mapping(target = "radius", expression = "java(upd != null && upd.getRadius() != null ? upd.getRadius() : src.getRadius())")
    @Mapping(target = "status", expression = "java(upd != null && upd.getStatus() != null ? upd.getStatus() : src.getStatus())")
    Place updatePlace(Place src, UpdatePlaceAdminRequest upd);

    CircleSearchArea mapToCircleSearchArea(BigDecimal lat, BigDecimal lon, int radius);
}
