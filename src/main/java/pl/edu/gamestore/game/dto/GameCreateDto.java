package pl.edu.gamestore.game.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record GameCreateDto(
        @NotBlank(message = "Title is required")
        @Size(max = 100)
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal price,

        @NotNull(message = "Release date is required")
        LocalDate releaseDate,

        @NotBlank(message = "Image URL is required")
        @Size(max = 512)
        String imageUrl,

        @NotEmpty(message = "At least one genre must be selected")
        Set<Long> genreIds,

        @NotEmpty(message = "At least one platform must be selected")
        Set<Long> platformIds
) {
}
