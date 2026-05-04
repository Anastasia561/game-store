package pl.edu.gamestore.genre.mapper;

import org.mapstruct.Mapper;
import pl.edu.gamestore.genre.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    default String map(Genre genre) {
        return (genre != null) ? genre.getName() : null;
    }
}
