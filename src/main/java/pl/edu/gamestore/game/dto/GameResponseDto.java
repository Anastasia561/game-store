package pl.edu.gamestore.game.dto;

import pl.edu.gamestore.encryption.HashId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record GameResponseDto(
        HashId id,
        String title,
        String description,
        BigDecimal price,
        LocalDate releaseDate,
        String imageUrl,
        Set<String> genres,
        Set<String> platforms
) {
}
