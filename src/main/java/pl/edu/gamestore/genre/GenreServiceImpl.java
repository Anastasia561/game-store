package pl.edu.gamestore.genre;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.genre.dto.GenreResponseDto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public Set<Genre> findAllByIds(Set<HashId> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Long> longIds = ids.stream()
                .map(HashId::value)
                .collect(Collectors.toSet());

        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(longIds));

        if (genres.size() != ids.size()) {
            throw new EntityNotFoundException("One or more genres could not be found.");
        }

        return genres;
    }

    @Override
    public List<GenreResponseDto> getAll() {
        return genreRepository.findAll()
                .stream().map(genreMapper::toDto).toList();
    }
}
