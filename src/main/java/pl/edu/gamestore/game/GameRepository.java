package pl.edu.gamestore.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
}
