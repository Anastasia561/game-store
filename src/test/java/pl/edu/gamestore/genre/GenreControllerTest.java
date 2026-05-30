package pl.edu.gamestore.genre;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.gamestore.AbstractControllerIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GenreControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldReturnListOfGenres_whenExist() throws Exception {
        performRequest(HttpMethod.GET, "/genres", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(5))

                .andExpect(jsonPath("$.data[0].name").value("Action"))
                .andExpect(jsonPath("$.data[1].name").value("RPG"));
    }
}
