package pl.edu.gamestore.platform;

import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;

import java.util.List;
import java.util.Set;

public interface PlatformService {
    Set<Platform> findAllByIds(Set<HashId> ids);

    List<PlatformResponseDto> findAll();
}
