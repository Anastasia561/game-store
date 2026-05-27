package pl.edu.gamestore.platform;

import org.mapstruct.Mapper;
import pl.edu.gamestore.config.HashIdMapper;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;

@Mapper(componentModel = "spring", uses = HashIdMapper.class)
public interface PlatformMapper {
    default String map(Platform platform) {
        return (platform != null) ? platform.getName() : null;
    }

    PlatformResponseDto toDto(Platform platform);
}
