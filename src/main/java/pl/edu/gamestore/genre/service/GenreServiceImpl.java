package pl.edu.gamestore.genre.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.genre.model.Genre;
import pl.edu.gamestore.genre.repository.GenreRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Set<Genre> findAllByIds(Set<Long> ids) {
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(ids));

        Set<Long> foundIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Long> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException("Genres not found: " + missingIds);
        }
        return genres;
    }
}
