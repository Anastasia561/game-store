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
import pl.edu.gamestore.genre.repository.GenreRepository;
import pl.edu.gamestore.platform.model.Platform;
import pl.edu.gamestore.platform.repository.PlatformRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
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

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.genreIds()));
        Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(dto.platformIds()));

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

        if (dto.genreIds() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.genreIds()));
            entity.getGenres().clear();
            entity.getGenres().addAll(genres);
        }

        if (dto.platformIds() != null) {
            Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(dto.platformIds()));
            entity.getPlatforms().clear();
            entity.getPlatforms().addAll(platforms);
        }
        return gameMapper.toDto(entity);
    }
}
