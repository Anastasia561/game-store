package pl.edu.gamestore.platform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.gamestore.encryption.HashIdMapper;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;

import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlatformMapperTest {
    private PlatformMapper platformMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        platformMapper = Mappers.getMapper(PlatformMapper.class);
        HashIdMapper hashIdMapper = new HashIdMapper();

        Field hashIdMapperField = platformMapper.getClass().getDeclaredField("hashIdMapper");
        hashIdMapperField.setAccessible(true);
        hashIdMapperField.set(platformMapper, hashIdMapper);
    }

    @Test
    void shouldMapPlatformToName() {
        Platform platform = new Platform();
        platform.setName("PlayStation 5");
        String result = platformMapper.map(platform);
        assertThat(result).isEqualTo("PlayStation 5");
    }

    @Test
    void shouldReturnNullWhenPlatformIsNull() {
        assertNull(platformMapper.map(null));
    }

    @Test
    void shouldMapToDto() {
        Platform platform = new Platform();
        platform.setName("Action");
        platform.setId(1L);

        PlatformResponseDto result = platformMapper.toDto(platform);

        assertEquals(1L, result.id().value());
        assertEquals("Action", result.name());
    }

    @Test
    void shouldReturnNullWhenGenreIsNullForDto() {
        assertNull(platformMapper.toDto(null));
    }
}
