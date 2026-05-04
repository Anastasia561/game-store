package pl.edu.gamestore.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.gamestore.game.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
