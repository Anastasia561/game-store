package pl.edu.gamestore.auth.service;

import pl.edu.gamestore.auth.dto.AuthRequestDto;
import pl.edu.gamestore.auth.dto.TokenResponseDto;

public interface AuthService {
    TokenResponseDto login(AuthRequestDto request);

    TokenResponseDto refresh(String refreshToken);

    void logout(String refreshToken);
}
