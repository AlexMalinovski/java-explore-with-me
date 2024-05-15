package ru.practicum.explorewithme.basic.service.locations.services;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.service.locations.models.Place;

import java.util.List;

public interface PlaceService {

    @NonNull
    Place createAndPublishPlace(@NonNull Place place);

    @NonNull
    Place updatePlaceAdmin(long placeId, @NonNull UpdatePlaceAdminRequest request);

    void deletePlace(long placeId);

    @NonNull
    Place createPlace(long userId, @NonNull Place place);

    @NonNull
    Place getPlaceById(long placeId);

    @NonNull
    List<Place> getPlaces(@NonNull PlaceFilterRequest request, int from, int size);

    @NonNull
    List<Place> getPlaces(@NonNull PlaceFilterRequest request);

    @NonNull
    List<Place> updatePlaces(@NonNull ChangePlacesRequest request);
}
