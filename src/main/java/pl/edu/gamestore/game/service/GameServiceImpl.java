package pl.edu.gamestore.game.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.mapper.GameMapper;
import pl.edu.gamestore.game.model.Game;
import pl.edu.gamestore.game.repository.GameRepository;
import pl.edu.gamestore.game.repository.GameSpecification;
import pl.edu.gamestore.genre.model.Genre;
import pl.edu.gamestore.genre.service.GenreService;
import pl.edu.gamestore.platform.model.Platform;
import pl.edu.gamestore.platform.service.PlatformService;

import java.util.HashSet;
import java.util.Set;

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
    public GameResponseDto findById(Long id) {
        return gameRepository.findById(id).map(gameMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }

    @Override
    @Transactional
    public Long create(GameRequestDto dto) {
        Game game = gameMapper.toEntity(dto);

        Set<Genre> genres = genreService.findAllByIds(dto.genreIds());
        Set<Platform> platforms = new HashSet<>(platformService.findAllByIds(dto.platformIds()));

        game.setGenres(genres);
        game.setPlatforms(platforms);

        return gameRepository.save(game).getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found");
        }
        gameRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GameResponseDto update(Long id, GameRequestDto dto) {
        Game entity = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        gameMapper.updateEntityFromDto(dto, entity);

        Set<Genre> genres = genreService.findAllByIds(dto.genreIds());
        Set<Platform> platforms = new HashSet<>(platformService.findAllByIds(dto.platformIds()));

        entity.getGenres().removeIf(g -> !dto.genreIds().contains(g.getId()));
        genres.forEach(g -> {
            if (entity.getGenres().stream().noneMatch(e -> e.getId().equals(g.getId()))) {
                entity.getGenres().add(g);
            }
        });

        entity.getPlatforms().removeIf(p -> !dto.platformIds().contains(p.getId()));
        platforms.forEach(p -> {
            if (entity.getPlatforms().stream().noneMatch(e -> e.getId().equals(p.getId()))) {
                entity.getPlatforms().add(p);
            }
        });

        return gameMapper.toDto(entity);
    }
}
