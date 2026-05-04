package pl.edu.gamestore.auth.dto;

public record TokenResponseDto (String accessToken, String refreshToken) {
}
