package pl.edu.gamestore.platform;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class PlatformServiceImpl implements PlatformService {
    private final PlatformRepository platformRepository;
    private final PlatformMapper platformMapper;

    @Override
    public Set<Platform> findAllByIds(Set<HashId> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> longIds = ids.stream()
                .map(HashId::value)
                .collect(Collectors.toSet());

        Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(longIds));

        if (platforms.size() != ids.size()) {
            throw new EntityNotFoundException("One or more platform could not be found.");
        }

        return platforms;
    }

    @Override
    public List<PlatformResponseDto> findAll() {
        return platformRepository.findAll()
                .stream().map(platformMapper::toDto).toList();
    }
}
