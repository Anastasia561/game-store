package pl.edu.gamestore.genre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.gamestore.genre.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
