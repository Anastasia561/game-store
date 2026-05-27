package pl.edu.gamestore.genre;

import org.springframework.data.jpa.repository.JpaRepository;

interface GenreRepository extends JpaRepository<Genre, Long> {
}
