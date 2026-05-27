package pl.edu.gamestore.genre;

import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.genre.dto.GenreResponseDto;

import java.util.List;
import java.util.Set;

public interface GenreService {
    Set<Genre> findAllByIds(Set<HashId> ids);
    List<GenreResponseDto> getAll();
}
