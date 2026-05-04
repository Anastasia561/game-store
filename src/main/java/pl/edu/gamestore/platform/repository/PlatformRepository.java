package pl.edu.gamestore.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.gamestore.platform.model.Platform;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
}
