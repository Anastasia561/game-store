package pl.edu.gamestore.game.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameResponseDto;

public interface GameService {
    Page<GameResponseDto> findAll(GameFilterDto filter, Pageable pageable);

    GameResponseDto findById(Long id);
}
