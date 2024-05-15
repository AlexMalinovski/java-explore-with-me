package ru.practicum.explorewithme.basic.service.categories.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exceptions.api.NotFoundException;
import ru.practicum.explorewithme.basic.service.categories.models.Category;
import ru.practicum.explorewithme.basic.service.categories.repositories.CategoriesRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriesServiceImplTest {

    @Mock
    private CategoriesRepository categoriesRepository;

    @InjectMocks
    private CategoriesServiceImpl categoriesService;

    @Test
    void createCategory_ifWithId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoriesService.createCategory(Category.builder().id(1L).build()));

        verify(categoriesRepository, never()).save(any());
    }

    @Test
    void createCategory() {
        Category expected = Category.builder().build();
        when(categoriesRepository.save(any())).thenReturn(expected);

        var actual = categoriesService.createCategory(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoriesRepository).save(expected);
    }

    @Test
    void deleteCategory_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> categoriesService.deleteCategory(-1L));
        assertThrows(NotFoundException.class, () -> categoriesService.deleteCategory(0L));

        verify(categoriesRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory() {
        long expected = 1L;

        categoriesService.deleteCategory(expected);

        verify(categoriesRepository).deleteById(expected);
    }

    @Test
    void updateCategory_ifWithoutId_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> categoriesService.updateCategory(Category.builder().build()));

        verify(categoriesRepository, never()).save(any());
    }

    @Test
    void updateCategory_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> categoriesService.updateCategory(Category.builder().id(-1L).build()));
        assertThrows(NotFoundException.class, () -> categoriesService.updateCategory(Category.builder().id(0L).build()));

        verify(categoriesRepository, never()).save(any());
    }

    @Test
    void updateCategory() {
        Category expected = Category.builder().id(1L).build();
        when(categoriesRepository.save(any())).thenReturn(expected);

        var actual = categoriesService.updateCategory(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoriesRepository).save(expected);
    }

    @Test
    void getCategories() {
        List<Category> expected = List.of(Category.builder().build());
        when(categoriesRepository.findAllWithOffsetAndLimit(anyInt(), anyInt())).thenReturn(expected);

        var actual = categoriesService.getCategories(0, 10);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoriesRepository).findAllWithOffsetAndLimit(0, 10);
    }

    @Test
    void getCategoryById_ifInvalidId_thenThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> categoriesService.getCategoryById(-1L));
        assertThrows(NotFoundException.class, () -> categoriesService.getCategoryById(0L));

        verify(categoriesRepository, never()).findById(any());
    }

    @Test
    void getCategoryById() {
        Category expected = Category.builder().build();
        when(categoriesRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        var actual = categoriesService.getCategoryById(1L);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoriesRepository).findById(1L);
    }

    @Test
    void getCategoryById_ifNotFound_thenThrowNotFoundException() {
        when(categoriesRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoriesService.getCategoryById(1L));

        verify(categoriesRepository).findById(1L);
    }
}