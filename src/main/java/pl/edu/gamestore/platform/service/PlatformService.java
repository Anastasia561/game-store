package pl.edu.gamestore.platform.service;

import pl.edu.gamestore.platform.model.Platform;

import java.util.Set;

public interface PlatformService {
    Set<Platform> findAllByIds(Set<Long> ids);
}
