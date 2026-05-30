package pl.edu.gamestore.genre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.gamestore.encryption.HashIdMapper;
import pl.edu.gamestore.genre.dto.GenreResponseDto;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenreMapperTest {
    private GenreMapper genreMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        genreMapper = Mappers.getMapper(GenreMapper.class);
        HashIdMapper hashIdMapper = new HashIdMapper();

        Field hashIdMapperField = genreMapper.getClass().getDeclaredField("hashIdMapper");
        hashIdMapperField.setAccessible(true);
        hashIdMapperField.set(genreMapper, hashIdMapper);
    }

    @Test
    void shouldMapGenreToName() {
        Genre genre = new Genre();
        genre.setName("Action");
        String result = genreMapper.map(genre);
        assertThat(result).isEqualTo("Action");
    }

    @Test
    void shouldReturnNullWhenGenreIsNullForName() {
        assertNull(genreMapper.map(null));
    }

    @Test
    void shouldMapToDto() {
        Genre genre = new Genre();
        genre.setName("Action");
        genre.setId(1L);

        GenreResponseDto result = genreMapper.toDto(genre);

        assertEquals(1L, result.id().value());
        assertEquals("Action", result.name());
    }

    @Test
    void shouldReturnNullWhenGenreIsNullForDto() {
        assertNull(genreMapper.toDto(null));
    }
}
