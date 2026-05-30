package pl.edu.gamestore.auth;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.edu.gamestore.auth.dto.AuthRequestDto;
import pl.edu.gamestore.auth.dto.TokenResponseDto;
import pl.edu.gamestore.auth.jwt.JwtService;
import pl.edu.gamestore.auth.refreshtoken.RefreshTokenService;
import pl.edu.gamestore.exception.InvalidRefreshTokenException;
import pl.edu.gamestore.person.Person;
import pl.edu.gamestore.person.PersonService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PersonService personService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_shouldReturnTokens_whenLoginWithValidCredentials() {
        AuthRequestDto request = new AuthRequestDto("test@mail.com", "password");

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@mail.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        Person person = new Person();
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateAccessToken(userDetails)).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(userDetails)).thenReturn(refreshToken);
        when(personService.getByEmail("test@mail.com")).thenReturn(person);

        TokenResponseDto response = authService.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());

        verify(refreshTokenService).create(person, refreshToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowBadCredentialsExceptionException_whenLoginWithInvalidCredentials() {
        AuthRequestDto request = new AuthRequestDto("wrong@mail.com", "wrong");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.login(request));

        assertEquals("Invalid email or password", exception.getMessage());

        verify(jwtService, never()).generateAccessToken(any());
        verify(refreshTokenService, never()).create(any(), any());
    }

    @Test
    void refresh_shouldReturnNewAccessToken_whenTokenIsValid() {
        String refreshToken = "valid-refresh-token";
        String username = "test@mail.com";
        String newAccessToken = "new-access-token";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(jwtService.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(true);
        when(jwtService.generateAccessToken(userDetails)).thenReturn(newAccessToken);

        TokenResponseDto response = authService.refresh(refreshToken);

        assertNotNull(response);
        assertEquals(newAccessToken, response.accessToken());
        assertEquals(refreshToken, response.refreshToken());

        verify(jwtService).isRefreshToken(refreshToken);
        verify(jwtService).extractUsername(refreshToken);
        verify(jwtService).isTokenValid(refreshToken, userDetails);
        verify(jwtService).generateAccessToken(userDetails);
    }

    @Test
    void refresh_shouldThrowInvalidRefreshTokenException_whenTokenNotRefreshToken() {
        String token = "not-refresh-token";

        when(jwtService.isRefreshToken(token)).thenReturn(false);

        InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(token));

        assertEquals("Token is not a refresh token", exception.getMessage());

        verify(jwtService).isRefreshToken(token);
        verify(jwtService, never()).extractUsername(any());
    }

    @Test
    void refresh_shouldThrowInvalidRefreshTokenException_whenTokenInvalid() {
        String refreshToken = "invalid-refresh-token";
        String username = "test@mail.com";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(jwtService.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(false);

        InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(refreshToken));

        assertEquals("Refresh token is invalid", exception.getMessage());

        verify(jwtService).isTokenValid(refreshToken, userDetails);
        verify(jwtService, never()).generateAccessToken(any());
    }

    @Test
    void refresh_shouldThrowInvalidRefreshTokenException_whenJwtExceptionOccurs() {
        String refreshToken = "broken-token";

        when(jwtService.isRefreshToken(refreshToken)).thenThrow(new JwtException("JWT error"));

        InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class,
                () -> authService.refresh(refreshToken));

        assertEquals("Invalid refresh token", exception.getMessage());
    }

    @Test
    void logout_shouldRevokeToken_whenTokenIsValid() {
        String refreshToken = "valid-refresh-token";
        String username = "test@mail.com";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(true);

        authService.logout(refreshToken);

        verify(jwtService).extractUsername(refreshToken);
        verify(jwtService).isRefreshToken(refreshToken);
        verify(jwtService).isTokenValid(refreshToken, userDetails);
        verify(refreshTokenService).revoke(refreshToken);
    }

    @Test
    void logout_shouldThrowInvalidRefreshTokenException_whenTokenNotRefreshTokenForLogout() {
        String token = "not-refresh-token";
        String username = "test@mail.com";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isRefreshToken(token)).thenReturn(false);

        InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class,
                () -> authService.logout(token));

        assertEquals("Invalid refresh token", exception.getMessage());

        verify(jwtService).isRefreshToken(token);
        verify(refreshTokenService, never()).revoke(any());
    }

    @Test
    void logout_shouldThrowInvalidRefreshTokenException_whenTokenInvalidForLogout() {
        String refreshToken = "invalid-refresh-token";
        String username = "test@mail.com";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isRefreshToken(refreshToken)).thenReturn(true);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(false);

        InvalidRefreshTokenException exception = assertThrows(InvalidRefreshTokenException.class,
                () -> authService.logout(refreshToken));

        assertEquals("Invalid refresh token", exception.getMessage());

        verify(jwtService).isTokenValid(refreshToken, userDetails);
        verify(refreshTokenService, never()).revoke(any());
    }
}
