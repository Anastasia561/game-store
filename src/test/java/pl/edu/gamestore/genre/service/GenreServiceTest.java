package pl.edu.gamestore.genre.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.gamestore.genre.model.Genre;
import pl.edu.gamestore.genre.repository.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void findAllByIds_shouldReturnGenres_whenAllIdsExist() {
        Set<Long> ids = Set.of(1L, 2L);

        Genre g1 = new Genre();
        g1.setId(1L);

        Genre g2 = new Genre();
        g2.setId(2L);

        when(genreRepository.findAllById(ids)).thenReturn(List.of(g1, g2));

        Set<Genre> result = genreService.findAllByIds(ids);

        assertEquals(2, result.size());
        assertTrue(result.contains(g1));
        assertTrue(result.contains(g2));

        verify(genreRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_shouldThrowException_whenSomeIdsMissing() {
        Set<Long> ids = Set.of(1L, 2L, 3L);

        Genre g1 = new Genre();
        g1.setId(1L);

        Genre g2 = new Genre();
        g2.setId(2L);

        when(genreRepository.findAllById(ids)).thenReturn(List.of(g1, g2));

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.findAllByIds(ids)
        );

        assertEquals("Genres not found: [3]", ex.getMessage());

        verify(genreRepository).findAllById(ids);
    }
}
