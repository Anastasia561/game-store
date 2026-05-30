package pl.edu.gamestore.genre;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.genre.dto.GenreResponseDto;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private GenreMapper genreMapper;
    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    void findAllByIds_shouldReturnGenres_whenAllIdsExist() {
        Set<Long> ids = Set.of(1L, 2L);
        Set<HashId> hashIds = Set.of(HashId.of(1L), HashId.of(2L));

        Genre g1 = new Genre();
        g1.setId(1L);

        Genre g2 = new Genre();
        g2.setId(2L);

        when(genreRepository.findAllById(ids)).thenReturn(List.of(g1, g2));

        Set<Genre> result = genreService.findAllByIds(hashIds);

        assertEquals(2, result.size());
        assertTrue(result.contains(g1));
        assertTrue(result.contains(g2));

        verify(genreRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_shouldThrowException_whenSomeIdsMissing() {
        Set<Long> ids = Set.of(1L, 2L, 3L);
        Set<HashId> hashIds = Set.of(HashId.of(1L), HashId.of(2L), HashId.of(3L));

        Genre g1 = new Genre();
        g1.setId(1L);

        Genre g2 = new Genre();
        g2.setId(2L);

        when(genreRepository.findAllById(ids)).thenReturn(List.of(g1, g2));

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.findAllByIds(hashIds)
        );

        assertEquals("One or more genres could not be found.", ex.getMessage());

        verify(genreRepository).findAllById(ids);
    }

    @Test
    void shouldReturnListOfGenreResponseDtos() {
        Genre genre = new Genre();
        GenreResponseDto responseDto = new GenreResponseDto(HashId.of(1L), "Steam");

        when(genreRepository.findAll()).thenReturn(List.of(genre));
        when(genreMapper.toDto(genre)).thenReturn(responseDto);

        List<GenreResponseDto> result = genreService.getAll();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0)).isEqualTo(responseDto);

        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).toDto(genre);
    }
}
