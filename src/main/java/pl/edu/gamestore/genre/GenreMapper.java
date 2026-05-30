package pl.edu.gamestore.genre;

import org.mapstruct.Mapper;
import pl.edu.gamestore.encryption.HashIdMapper;
import pl.edu.gamestore.genre.dto.GenreResponseDto;

@Mapper(componentModel = "spring", uses = HashIdMapper.class)
public interface GenreMapper {
    default String map(Genre genre) {
        return (genre != null) ? genre.getName() : null;
    }

    GenreResponseDto toDto(Genre genre);
}
