package pl.edu.gamestore.genre.service;

import pl.edu.gamestore.genre.model.Genre;

import java.util.Set;

public interface GenreService {
    Set<Genre> findAllByIds(Set<Long> ids);
}
