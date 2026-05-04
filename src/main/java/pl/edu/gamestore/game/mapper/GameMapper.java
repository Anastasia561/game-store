package pl.edu.gamestore.game.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.model.Game;
import pl.edu.gamestore.genre.mapper.GenreMapper;
import pl.edu.gamestore.platform.mapper.PlatformMapper;

@Mapper(componentModel = "spring", uses = {PlatformMapper.class, GenreMapper.class})
public interface GameMapper {
    GameResponseDto toDto(Game game);

    @Mapping(target = "id", ignore = true)
    Game toEntity(GameRequestDto dto);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "platforms", ignore = true)
    void updateEntityFromDto(GameRequestDto dto, @MappingTarget Game game);
}
