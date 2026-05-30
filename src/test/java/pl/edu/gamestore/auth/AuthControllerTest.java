package pl.edu.gamestore.auth;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.gamestore.AbstractControllerIntegrationTest;
import pl.edu.gamestore.auth.dto.AuthRequestDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends AbstractControllerIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void login_shouldLoginSuccessfully_whenCredentialsAreValid() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("john@gamestore.com", "111");

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }

    @Test
    void login_shouldReturn401_whenCredentialsDoNotExist() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("test@example.com", "111");

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturn400_whenCredentialsAreNotValid() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("test@gmail.com", null);

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.validationErrors").isArray())
                .andExpect(jsonPath("$.error.validationErrors.length()").value(1));
    }

    @Test
    void refreshToken_shouldRefreshToken_whenRefreshCookieIsPresent() throws Exception {
        AuthRequestDto loginDto = new AuthRequestDto("john@gamestore.com", "111");

        Cookie refreshCookie = performRequest(HttpMethod.POST, "/auth/login", loginDto)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getCookie("refreshToken");

        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(post("/auth/refresh").cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void refreshToken_shouldReturnUnauthorized_whenRefreshCookieIsMissing() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_shouldLogoutSuccessfully_whenRefreshCookieIsPresent() throws Exception {
        AuthRequestDto loginDto = new AuthRequestDto("john@gamestore.com", "111");

        MvcResult loginResponse = performRequest(HttpMethod.POST, "/auth/login", loginDto)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(loginResponse.getResponse().getContentAsString(), "$.accessToken");
        Cookie refreshCookie = loginResponse.getResponse().getCookie("refreshToken");

        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(post("/auth/logout")
                        .cookie(refreshCookie)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }
}
