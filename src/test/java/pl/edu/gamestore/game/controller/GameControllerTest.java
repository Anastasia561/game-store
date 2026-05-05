package pl.edu.gamestore.game.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.gamestore.AbstractControllerIntegrationTest;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.model.Game;
import pl.edu.gamestore.genre.model.Genre;
import pl.edu.gamestore.person.model.Role;
import pl.edu.gamestore.platform.model.Platform;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldReturnPageOfGames_whenGamesExist() throws Exception {
        performRequest(HttpMethod.GET, "/games", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(10))

                .andExpect(jsonPath("$.data.content[0].title").value("Elden Ring"))
                .andExpect(jsonPath("$.data.content[0].description").value("A massive open-world RPG."))
                .andExpect(jsonPath("$.data.content[0].price").value(59.99))
                .andExpect(jsonPath("$.data.content[0].releaseDate").value("2022-02-25"))
                .andExpect(jsonPath("$.data.content[0].imageUrl").value("http://store.com/elden.jpg"))
                .andExpect(jsonPath("$.data.content[0].platforms").isArray())
                .andExpect(jsonPath("$.data.content[0].platforms.length()").value(3))
                .andExpect(jsonPath("$.data.content[0].genres").isArray())
                .andExpect(jsonPath("$.data.content[0].genres.length()").value(1));
    }

    @Test
    void shouldReturnPageOfGames_whenSearchedByGenreAndPlatformNames() throws Exception {
        performRequest(HttpMethod.GET, "/games?genre={genre}&platform={platform}", null,
                "RPG", "PC")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(3));
    }

    @Test
    void shouldReturnPageOfGames_whenSearchedByGenreAndPlatformNamesAndTitle() throws Exception {
        performRequest(HttpMethod.GET, "/games?genre={genre}&platform={platform}&title={title}", null,
                "RPG", "PC", "Witch")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    @Test
    void shouldReturnGame_whenSearchedById() throws Exception {
        performRequest(HttpMethod.GET, "/games/{id}", null, 1)
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.data.title").value("Elden Ring"))
                .andExpect(jsonPath("$.data.description").value("A massive open-world RPG."))
                .andExpect(jsonPath("$.data.price").value(59.99))
                .andExpect(jsonPath("$.data.releaseDate").value("2022-02-25"))
                .andExpect(jsonPath("$.data.imageUrl").value("http://store.com/elden.jpg"))
                .andExpect(jsonPath("$.data.platforms").isArray())
                .andExpect(jsonPath("$.data.platforms.length()").value(3))
                .andExpect(jsonPath("$.data.genres").isArray())
                .andExpect(jsonPath("$.data.genres.length()").value(1));
    }

    @Test
    void shouldReturn404_whenGameNotFoundById() throws Exception {
        performRequest(HttpMethod.GET, "/games/{id}", null, 1000)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Game not found"));
    }

    @Test
    void shouldReturn401_whenCreateGameWithInvalidToken() throws Exception {
        mockMvc.perform(post("/games")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateGame_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        GameRequestDto dto = new GameRequestDto("Witcher 3", "Des", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L, 2L), Set.of(1L));

        MvcResult mvcResult = performRequest(HttpMethod.POST, "/games", dto)
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        Long id = root.path("data").asLong();

        Game game = em.createQuery("SELECT a FROM Game a WHERE a.id = :id",
                Game.class).setParameter("id", id).getSingleResult();

        assertEquals("Witcher 3", game.getTitle());
        assertEquals("Des", game.getDescription());
        assertEquals(BigDecimal.valueOf(20.4), game.getPrice());
        assertEquals(LocalDate.of(2026, 10, 2), game.getReleaseDate());
        assertEquals("url", game.getImageUrl());
        assertThat(game.getGenres()).extracting(Genre::getName).contains("Action", "RPG");
        assertThat(game.getPlatforms()).extracting(Platform::getName).contains("PC");
    }

    @Test
    void shouldReturn400_whenValidationErrorsInCreate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        GameRequestDto dto = new GameRequestDto("", null, BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(), Set.of(1L));

        performRequest(HttpMethod.POST, "/games", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.validationErrors").isArray())
                .andExpect(jsonPath("$.error.validationErrors.length()").value(3));
    }

    @Test
    void shouldReturn404_whenGameNotFoundForDelete() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        performRequest(HttpMethod.DELETE, "/games/{id}", null, 1000)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Game not found"));
    }

    @Test
    void shouldReturn401_whenDeleteGameWithInvalidToken() throws Exception {
        mockMvc.perform(delete("/games")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldDeleteGame_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.DELETE, "/games/{id}", null, 1)
                .andExpect(status().isNoContent());

        Game found = em.find(Game.class, 1);
        assertNull(found);
    }

    @Test
    void shouldReturn404_whenGameNotFoundForUpdate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        GameRequestDto dto = new GameRequestDto("Test", "Test desc", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L), Set.of(1L));

        performRequest(HttpMethod.PUT, "/games/{id}", dto, 1000)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Game not found"));
    }

    @Test
    void shouldReturn401_whenUpdateGameWithInvalidToken() throws Exception {
        mockMvc.perform(put("/games")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400_whenValidationErrorsInUpdate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        GameRequestDto dto = new GameRequestDto("", null, BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(), Set.of(1L));

        performRequest(HttpMethod.PUT, "/games/{id}", dto, 1)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Validation failed"))
                .andExpect(jsonPath("$.error.validationErrors").isArray())
                .andExpect(jsonPath("$.error.validationErrors.length()").value(3));
    }

    @Test
    void shouldUpdateGame_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        GameRequestDto dto = new GameRequestDto("Test", "Test desc", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L), Set.of(1L));

        performRequest(HttpMethod.PUT, "/games/{id}", dto, 1)
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.data.title").value("Test"))
                .andExpect(jsonPath("$.data.description").value("Test desc"))
                .andExpect(jsonPath("$.data.price").value(20.4))
                .andExpect(jsonPath("$.data.releaseDate").value("2026-10-02"))
                .andExpect(jsonPath("$.data.imageUrl").value("url"))
                .andExpect(jsonPath("$.data.platforms").isArray())
                .andExpect(jsonPath("$.data.platforms.length()").value(1))
                .andExpect(jsonPath("$.data.genres").isArray())
                .andExpect(jsonPath("$.data.genres.length()").value(1));
    }
}
