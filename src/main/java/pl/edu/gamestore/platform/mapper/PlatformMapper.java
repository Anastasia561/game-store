package pl.edu.gamestore.platform.mapper;

import org.mapstruct.Mapper;
import pl.edu.gamestore.platform.model.Platform;

@Mapper(componentModel = "spring")
public interface PlatformMapper {
    default String map(Platform platform) {
        return (platform != null) ? platform.getName() : null;
    }
}
