package ru.practicum.explorewithme.basic.locations.services;

import org.springframework.lang.NonNull;
import ru.practicum.explorewithme.basic.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.locations.models.Place;

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
