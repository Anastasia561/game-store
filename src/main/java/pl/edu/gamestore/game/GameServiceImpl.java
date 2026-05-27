package pl.edu.gamestore.game;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.genre.Genre;
import pl.edu.gamestore.genre.GenreService;
import pl.edu.gamestore.platform.Platform;
import pl.edu.gamestore.platform.PlatformService;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GenreService genreService;
    private final PlatformService platformService;
    private final GameMapper gameMapper;

    @Override
    public Page<GameResponseDto> findAll(GameFilterDto filter, Pageable pageable) {
        return gameRepository.findAll(GameSpecification.buildFilter(filter), pageable).map(gameMapper::toDto);
    }

    @Override
    public GameResponseDto findById(HashId id) {
        return gameRepository.findById(id.value()).map(gameMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }

    @Override
    @Transactional
    public HashId create(GameRequestDto dto) {
        Game game = gameMapper.toEntity(dto);

        Set<Genre> genres = genreService.findAllByIds(dto.genreIds());
        Set<Platform> platforms = new HashSet<>(platformService.findAllByIds(dto.platformIds()));

        game.setGenres(genres);
        game.setPlatforms(platforms);

        Game saved = gameRepository.save(game);

        return new HashId(saved.getId());
    }

    @Override
    @Transactional
    public void delete(HashId id) {
        if (!gameRepository.existsById(id.value())) {
            throw new EntityNotFoundException("Game not found");
        }
        gameRepository.deleteById(id.value());
    }

    @Override
    @Transactional
    public GameResponseDto update(HashId id, GameRequestDto dto) {
        Game entity = gameRepository.findById(id.value())
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        gameMapper.updateEntityFromDto(dto, entity);

        Set<Genre> incomingGenres = genreService.findAllByIds(dto.genreIds());
        Set<Platform> incomingPlatforms = platformService.findAllByIds(dto.platformIds());

        updateCollection(entity.getGenres(), incomingGenres, Genre::getId);
        updateCollection(entity.getPlatforms(), incomingPlatforms, Platform::getId);
        return gameMapper.toDto(entity);
    }

    private <T> void updateCollection(Set<T> currentSet, Set<T> incomingSet, Function<T, Long> idExtractor) {
        Set<Long> incomingIds = incomingSet.stream().map(idExtractor).collect(Collectors.toSet());

        currentSet.removeIf(item -> !incomingIds.contains(idExtractor.apply(item)));

        incomingSet.forEach(item -> {
            if (currentSet.stream().noneMatch(existing -> idExtractor.apply(existing).equals(idExtractor.apply(item)))) {
                currentSet.add(item);
            }
        });
    }
}
