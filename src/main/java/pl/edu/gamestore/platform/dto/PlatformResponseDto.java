package pl.edu.gamestore.platform.dto;

import pl.edu.gamestore.encryption.HashId;

public record PlatformResponseDto(
        HashId id,
        String name
) {
}
