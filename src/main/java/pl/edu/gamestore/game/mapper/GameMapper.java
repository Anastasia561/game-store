package pl.edu.gamestore.game.mapper;

import org.mapstruct.Mapper;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.model.Game;
import pl.edu.gamestore.genre.mapper.GenreMapper;
import pl.edu.gamestore.platform.mapper.PlatformMapper;

@Mapper(componentModel = "spring", uses = {PlatformMapper.class, GenreMapper.class})
public interface GameMapper {
    GameResponseDto toDto(Game game);
}
