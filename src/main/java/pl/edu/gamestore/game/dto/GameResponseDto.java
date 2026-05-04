package pl.edu.gamestore.game.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record GameResponseDto(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDate releaseDate,
        String imageUrl,
        Set<String> genres,
        Set<String> platforms
) {
}
