package pl.edu.gamestore.genre.dto;

import pl.edu.gamestore.encryption.HashId;

public record GenreResponseDto(
        HashId id,
        String name
) {
}
