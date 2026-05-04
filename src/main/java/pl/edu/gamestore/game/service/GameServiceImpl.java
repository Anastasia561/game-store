package pl.edu.gamestore.game.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.mapper.GameMapper;
import pl.edu.gamestore.game.repository.GameRepository;

@Service
@RequiredArgsConstructor
class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public Page<GameResponseDto> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable).map(gameMapper::toDto);
    }

    @Override
    public GameResponseDto findById(Long id) {
        return gameRepository.findById(id).map(gameMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }
}
