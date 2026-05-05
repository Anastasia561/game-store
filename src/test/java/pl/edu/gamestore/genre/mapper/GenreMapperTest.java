package pl.edu.gamestore.genre.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.gamestore.genre.model.Genre;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GenreMapperTest {
    private final GenreMapper mapper = Mappers.getMapper(GenreMapper.class);

    @Test
    void shouldMapPlatformToName() {
        Genre genre = new Genre();
        genre.setName("Action");
        String result = mapper.map(genre);
        assertThat(result).isEqualTo("Action");
    }

    @Test
    void shouldReturnNullWhenPlatformIsNull() {
        assertNull(mapper.map(null));
    }
}
