package pl.edu.gamestore.game.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.gamestore.game.GameMapper;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.Game;
import pl.edu.gamestore.genre.GenreMapper;
import pl.edu.gamestore.genre.Genre;
import pl.edu.gamestore.platform.PlatformMapper;
import pl.edu.gamestore.platform.Platform;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameMapperTest {
    private GameMapper gameMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        gameMapper = Mappers.getMapper(GameMapper.class);
        GenreMapper genreMapper = Mappers.getMapper(GenreMapper.class);
        PlatformMapper platformMapper = Mappers.getMapper(PlatformMapper.class);

        Field genreMapperField = gameMapper.getClass().getDeclaredField("genreMapper");
        genreMapperField.setAccessible(true);
        genreMapperField.set(gameMapper, genreMapper);

        Field platformMapperField = gameMapper.getClass().getDeclaredField("platformMapper");
        platformMapperField.setAccessible(true);
        platformMapperField.set(gameMapper, platformMapper);
    }

    @Test
    void shouldMapToDto_whenInputIsValid() {
        Game game = new Game();
        game.setTitle("Test");
        game.setDescription("Test description");
        game.setImageUrl("test url");
        game.setPrice(BigDecimal.valueOf(20.4));
        game.setReleaseDate(LocalDate.of(2026, 10, 10));

        Genre genre = new Genre();
        genre.setName("Test genre");

        Platform platform = new Platform();
        platform.setName("Test platform");

        game.setPlatforms(Set.of(platform));
        game.setGenres(Set.of(genre));

        GameResponseDto dto = gameMapper.toDto(game);

        assertThat(dto).isNotNull();
        assertThat(dto.title()).isEqualTo("Test");
        assertThat(dto.description()).isEqualTo("Test description");
        assertThat(dto.imageUrl()).isEqualTo("test url");
        assertThat(dto.price()).isEqualByComparingTo(BigDecimal.valueOf(20.4));
        assertThat(dto.releaseDate()).isEqualTo(LocalDate.of(2026, 10, 10));

        assertThat(dto.genres()).containsExactly("Test genre");
        assertThat(dto.platforms()).containsExactly("Test platform");
    }

    @Test
    void shouldMapToEntity_whenInputIsValid() {
        GameRequestDto dto = new GameRequestDto("Title", "Description",
                BigDecimal.valueOf(10), LocalDate.of(2025, 1, 1),
                "img-url", Set.of(1L), Set.of(2L));

        Game entity = gameMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo("Title");
        assertThat(entity.getDescription()).isEqualTo("Description");
        assertThat(entity.getImageUrl()).isEqualTo("img-url");
        assertThat(entity.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10));
        assertThat(entity.getReleaseDate()).isEqualTo(LocalDate.of(2025, 1, 1));

        assertThat(entity.getGenres()).isNullOrEmpty();
        assertThat(entity.getPlatforms()).isNullOrEmpty();
    }

    @Test
    void shouldUpdateEntityFields_whenInputIsValid() {
        Game game = new Game();
        game.setTitle("Old");

        GameRequestDto dto = new GameRequestDto("New", "Desc", BigDecimal.ONE,
                LocalDate.of(2024, 1, 1),
                "img", Set.of(1L), Set.of(2L));

        gameMapper.updateEntityFromDto(dto, game);

        assertThat(game.getTitle()).isEqualTo("New");
        assertThat(game.getDescription()).isEqualTo("Desc");
    }

    @Test
    void shouldNotUpdateGenresAndPlatforms() {
        Game game = new Game();

        Genre genre = new Genre();
        genre.setName("Action");

        Platform platform = new Platform();
        platform.setName("PC");

        game.setGenres(new HashSet<>(Set.of(genre)));
        game.setPlatforms(new HashSet<>(Set.of(platform)));

        GameRequestDto dto = new GameRequestDto("New", "Desc", BigDecimal.ONE,
                LocalDate.of(2024, 1, 1),
                "img", Set.of(1L), Set.of(2L));

        gameMapper.updateEntityFromDto(dto, game);

        assertThat(game.getGenres()).containsExactly(genre);
        assertThat(game.getPlatforms()).containsExactly(platform);
    }

    @Test
    void shouldReturnNull_whenDtoIsNull() {
        assertNull(gameMapper.toEntity(null));
    }

    @Test
    void shouldReturnNull_whenEntityIsNull() {
        assertNull(gameMapper.toDto(null));
    }
}
