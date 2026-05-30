package pl.edu.gamestore.auth.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
