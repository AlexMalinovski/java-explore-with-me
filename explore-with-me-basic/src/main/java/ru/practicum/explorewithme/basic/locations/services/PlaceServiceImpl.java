package ru.practicum.explorewithme.basic.locations.services;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.api.ForbiddenException;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.locations.mappers.PlaceFilterMapper;
import ru.practicum.explorewithme.basic.locations.models.Place;
import ru.practicum.explorewithme.basic.locations.repositories.PlacesRepository;
import ru.practicum.explorewithme.basic.users.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final String notFoundMsg = "Place was not found";
    private final PlacesRepository placesRepository;
    private final UserRepository userRepository;
    private final LocationMapper locationMapper;
    private final PlaceFilterMapper placeFilterMapper;

    @Override
    @NonNull
    public Place createAndPublishPlace(@NonNull Place place) {
        if (place.getId() != null) {
            throw new IllegalArgumentException("id can be null.");
        }
        return placesRepository.save(place
                .toBuilder()
                .status(PlaceStatus.PUBLISHED)
                .build());
    }

    @Override
    @NonNull
    @Transactional
    public Place updatePlaceAdmin(long placeId, @NonNull final UpdatePlaceAdminRequest request) {
        if (placeId <= 0L) {
            throw new NotFoundException(notFoundMsg);
        }
        Place updatedPlace = placesRepository.findById(placeId)
                .map(place -> locationMapper.updatePlace(place, request))
                .orElseThrow(() -> new NotFoundException(notFoundMsg));

        return placesRepository.save(updatedPlace);
    }

    @Override
    public void deletePlace(long placeId) {
        if (placeId <= 0L) {
            throw new NotFoundException(notFoundMsg);
        }
        placesRepository.deleteById(placeId);
    }

    @Override
    @NonNull
    public Place createPlace(long userId, @NonNull Place place) {
        if (place.getId() != null) {
            throw new IllegalArgumentException("id can be null.");
        }
        if (!userRepository.existsById(userId)) {
            throw new ForbiddenException("User not found");
        }
        return placesRepository.save(place);
    }

    @Override
    @NonNull
    public Place getPlaceById(long placeId) {
        if (placeId <= 0L) {
            throw new NotFoundException(notFoundMsg);
        }
        return placesRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException(notFoundMsg));
    }

    @Override
    @NonNull
    public List<Place> getPlaces(@NonNull PlaceFilterRequest request, int from, int size) {
        BooleanExpression byFilter = placeFilterMapper.toPlaceFilter(request);
        OrderSpecifier<?> order = placeFilterMapper.toPlaceOrder(request);

        return placesRepository.findByWithOffsetAndLimit(byFilter, order, from, size);
    }

    @Override
    @NonNull
    public List<Place> getPlaces(@NonNull PlaceFilterRequest request) {
        BooleanExpression byFilter = placeFilterMapper.toPlaceFilter(request);
        Iterable<Place> places = placesRepository.findAll(byFilter);

        return StreamSupport.stream(places.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional
    public List<Place> updatePlaces(@NonNull ChangePlacesRequest request) {
        List<Place> places = placesRepository.findAllById(request.getPlaceIds())
                .stream()
                .map(place -> {
                    if (request.getNewStatus() != null) {
                        return place.toBuilder().status(request.getNewStatus()).build();
                    }
                    return place;
                })
                .collect(Collectors.toList());
        if (places.size() != request.getPlaceIds().size()) {
            throw new NotFoundException(notFoundMsg);
        }

        return placesRepository.saveAll(places);
    }

}
