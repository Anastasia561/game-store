package pl.edu.gamestore.game.dto;

public record GameFilterDto(
        String title,
        String genre,
        String platform
) {
}
