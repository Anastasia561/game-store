package pl.edu.gamestore.platform.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.platform.model.Platform;
import pl.edu.gamestore.platform.repository.PlatformRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class PlatformServiceImpl implements PlatformService {
    private final PlatformRepository platformRepository;

    @Override
    public Set<Platform> findAllByIds(Set<Long> ids) {
        Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(ids));

        Set<Long> foundIds = platforms.stream()
                .map(Platform::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException("Platforms not found: " + missingIds);
        }
        return platforms;
    }
}
