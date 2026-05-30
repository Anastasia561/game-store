package pl.edu.gamestore.platform;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.gamestore.AbstractControllerIntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PlatformControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldReturnListOfPlatforms_whenExist() throws Exception {
        performRequest(HttpMethod.GET, "/platforms", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(5))

                .andExpect(jsonPath("$.data[0].name").value("PC"))
                .andExpect(jsonPath("$.data[1].name").value("PlayStation 5"));
    }
}
