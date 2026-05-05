package pl.edu.gamestore.platform.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.gamestore.platform.model.Platform;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlatformMapperTest {
    private final PlatformMapper mapper = Mappers.getMapper(PlatformMapper.class);

    @Test
    void shouldMapPlatformToName() {
        Platform platform = new Platform();
        platform.setName("PlayStation 5");
        String result = mapper.map(platform);
        assertThat(result).isEqualTo("PlayStation 5");
    }

    @Test
    void shouldReturnNullWhenPlatformIsNull() {
        assertNull(mapper.map(null));
    }
}
