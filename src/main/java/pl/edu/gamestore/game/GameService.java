package pl.edu.gamestore.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameResponseDto;

public interface GameService {
    Page<GameResponseDto> findAll(GameFilterDto filter, Pageable pageable);

    GameResponseDto findById(HashId id);

    HashId create(GameRequestDto dto);

    void delete(HashId id);

    GameResponseDto update(HashId id, GameRequestDto dto);
}
