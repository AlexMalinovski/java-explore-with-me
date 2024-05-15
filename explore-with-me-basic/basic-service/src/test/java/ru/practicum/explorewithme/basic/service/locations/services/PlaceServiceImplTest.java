package ru.practicum.explorewithme.basic.service.locations.services;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exceptions.api.ForbiddenException;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.ChangePlacesRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.PlaceFilterRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.dto.UpdatePlaceAdminRequest;
import ru.practicum.explorewithme.basic.lib.dto.locations.enums.PlaceStatus;
import ru.practicum.explorewithme.basic.service.locations.mappers.LocationMapper;
import ru.practicum.explorewithme.basic.service.locations.mappers.PlaceFilterMapper;
import ru.practicum.explorewithme.basic.service.locations.models.Place;
import ru.practicum.explorewithme.basic.service.locations.repositories.PlacesRepository;
import ru.practicum.explorewithme.basic.service.users.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

    @Mock
    private PlacesRepository placesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationMapper locationMapper;

    @Mock
    private PlaceFilterMapper placeFilterMapper;

    @InjectMocks
    private PlaceServiceImpl placeService;

    @Test
    void createAndPublishPlace_ifIdNotNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> placeService.createAndPublishPlace(Place.builder().id(2L).build()));

        verify(placesRepository, never()).save(any());
    }

    @Test
    void createAndPublishPlace_ifInvoke_thenSavePublished() {
        Place expected = Place.builder().build();
        ArgumentCaptor<Place> captor = ArgumentCaptor.forClass(Place.class);
        when(placesRepository.save(any(Place.class))).thenReturn(expected);

        var actual = placeService.createAndPublishPlace(expected);

        verify(placesRepository).save(captor.capture());
        assertEquals(PlaceStatus.PUBLISHED, captor.getValue().getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void updatePlaceAdmin_ifNotFound_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> placeService.updatePlaceAdmin(0, UpdatePlaceAdminRequest.builder().build()));

        when(placesRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> placeService.updatePlaceAdmin(10L, UpdatePlaceAdminRequest.builder().build()));

        verify(placesRepository, never()).save(any());
    }

    @Test
    void updatePlaceAdmin_ifInvoke_thenSaveUpdated() {
        Place expected = Place.builder().id(10L).build();
        UpdatePlaceAdminRequest request = UpdatePlaceAdminRequest.builder().build();
        when(placesRepository.findById(anyLong())).thenReturn(Optional.of(expected));
        when(locationMapper.updatePlace(any(Place.class), any(UpdatePlaceAdminRequest.class))).thenReturn(expected);
        when(placesRepository.save(any())).thenReturn(expected);

        var actual = placeService.updatePlaceAdmin(10L, request);

        verify(placesRepository).findById(10L);
        verify(locationMapper).updatePlace(expected, request);
        verify(placesRepository).save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void deletePlace_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> placeService.deletePlace(-1L));

        assertThrows(NotFoundException.class,
                () -> placeService.deletePlace(0L));

        verify(placesRepository, never()).deleteById(any());
    }

    @Test
    void deletePlace() {
        placeService.deletePlace(10L);
        verify(placesRepository).deleteById(10L);
    }

    @Test
    void createPlace_ifIdNotNull_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> placeService.createAndPublishPlace(Place.builder().id(2L).build()));

        verify(placesRepository, never()).save(any());
    }

    @Test
    void createPlace_ifUserNotFound_thenThrowForbiddenException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ForbiddenException.class,
                () -> placeService.createPlace(10L, Place.builder().build()));

        verify(userRepository).existsById(10L);
        verify(placesRepository, never()).save(any());
    }

    @Test
    void createPlace() {
        Place expected = Place.builder().build();
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(placesRepository.save(any())).thenReturn(expected);

        var actual = placeService.createPlace(10L, expected);

        assertNotNull(actual);
        verify(placesRepository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getPlaceById_ifNotFound_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> placeService.getPlaceById(-1L));
        assertThrows(NotFoundException.class,
                () -> placeService.getPlaceById(0L));
        verify(placesRepository, never()).findById(anyLong());

        when(placesRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> placeService.getPlaceById(1L));
        verify(placesRepository).findById(1L);
    }

    @Test
    void getPlaceById() {
        Place expected = Place.builder().build();
        when(placesRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        var actual = placeService.getPlaceById(1L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(placesRepository).findById(1L);
    }

    @Test
    void getPlaces() {
        List<Place> expected = List.of();
        PlaceFilterRequest request = PlaceFilterRequest.builder().build();
        when(placeFilterMapper.toPlaceFilter(any(PlaceFilterRequest.class))).thenReturn(Expressions.TRUE.isTrue());
        when(placesRepository.findAll(any(BooleanExpression.class))).thenReturn(expected);

        var actual = placeService.getPlaces(request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(placeFilterMapper).toPlaceFilter(request);
        verify(placesRepository).findAll(any(BooleanExpression.class));
    }

    @Test
    void updatePlaces_ifPlaceNotFound_thenThrowNotFoundException() {
        Set<Long> expected = Set.of(1L, 2L);
        when(placesRepository.findAllById(anySet())).thenReturn(List.of(Place.builder().id(1L).build()));

        assertThrows(NotFoundException.class,
                () -> placeService.updatePlaces(ChangePlacesRequest.builder().placeIds(expected).build()));

        verify(placesRepository).findAllById(expected);
        verify(placesRepository, never()).saveAll(any());
    }

    @Test
    void updatePlaces() {
        ChangePlacesRequest request = ChangePlacesRequest.builder().placeIds(Set.of(1L)).newStatus(PlaceStatus.PUBLISHED).build();
        List<Place> expected = List.of(Place.builder().id(1L).build());
        when(placesRepository.findAllById(anySet())).thenReturn(List.of(Place.builder().id(1L).build()));
        when(placesRepository.saveAll(any())).thenReturn(expected);
        ArgumentCaptor<List<Place>> captor = ArgumentCaptor.forClass(List.class);

        var actual = placeService.updatePlaces(request);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(placesRepository).findAllById(request.getPlaceIds());
        verify(placesRepository).saveAll(captor.capture());
        var result = captor.getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
        assertEquals(request.getNewStatus(), result.get(0).getStatus());
    }
}