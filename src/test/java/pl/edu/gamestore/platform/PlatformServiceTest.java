package pl.edu.gamestore.platform.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.gamestore.platform.Platform;
import pl.edu.gamestore.platform.PlatformRepository;
import pl.edu.gamestore.platform.PlatformServiceImpl;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlatformServiceTest {
    @Mock
    private PlatformRepository platformRepository;
    @InjectMocks
    private PlatformServiceImpl platformService;

    @Test
    void findAllByIds_shouldReturnPlatforms_whenAllIdsExist() {
        Set<Long> ids = Set.of(1L, 2L);

        Platform p1 = new Platform();
        p1.setId(1L);

        Platform p2 = new Platform();
        p2.setId(2L);

        when(platformRepository.findAllById(ids)).thenReturn(List.of(p1, p2));

        Set<Platform> result = platformService.findAllByIds(ids);

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));

        verify(platformRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_shouldThrowException_whenSomeIdsMissing() {
        Set<Long> ids = Set.of(1L, 2L, 3L);

        Platform p1 = new Platform();
        p1.setId(1L);

        Platform p2 = new Platform();
        p2.setId(2L);

        when(platformRepository.findAllById(ids)).thenReturn(List.of(p1, p2));

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> platformService.findAllByIds(ids)
        );

        assertEquals("Platforms not found: [3]", ex.getMessage());

        verify(platformRepository).findAllById(ids);
    }
}
