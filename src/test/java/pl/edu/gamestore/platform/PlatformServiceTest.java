package pl.edu.gamestore.platform;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.gamestore.encryption.HashId;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {
    @Mock
    private PlatformRepository platformRepository;
    @Mock
    private PlatformMapper platformMapper;
    @InjectMocks
    private PlatformServiceImpl platformService;

    @Test
    void findAllByIds_shouldReturnPlatforms_whenAllIdsExist() {
        Set<Long> ids = Set.of(1L, 2L);
        Set<HashId> hashIds = Set.of(HashId.of(1L), HashId.of(2L));

        Platform p1 = new Platform();
        p1.setId(1L);

        Platform p2 = new Platform();
        p2.setId(2L);

        when(platformRepository.findAllById(ids)).thenReturn(List.of(p1, p2));

        Set<Platform> result = platformService.findAllByIds(hashIds);

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));

        verify(platformRepository).findAllById(ids);
    }

    @Test
    void findAllByIds_shouldThrowException_whenSomeIdsMissing() {
        Set<Long> ids = Set.of(1L, 2L, 3L);
        Set<HashId> hashIds = Set.of(HashId.of(1L), HashId.of(2L), HashId.of(3L));

        Platform p1 = new Platform();
        p1.setId(1L);

        Platform p2 = new Platform();
        p2.setId(2L);

        when(platformRepository.findAllById(ids)).thenReturn(List.of(p1, p2));

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> platformService.findAllByIds(hashIds)
        );

        assertEquals("One or more platform could not be found.", ex.getMessage());
        verify(platformRepository).findAllById(ids);
    }

    @Test
    void shouldReturnListOfPlatformResponseDtos() {
        Platform platform = new Platform();
        PlatformResponseDto responseDto = new PlatformResponseDto(HashId.of(1L), "Steam");

        when(platformRepository.findAll()).thenReturn(List.of(platform));
        when(platformMapper.toDto(platform)).thenReturn(responseDto);

        List<PlatformResponseDto> result = platformService.findAll();

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0)).isEqualTo(responseDto);

        verify(platformRepository, times(1)).findAll();
        verify(platformMapper, times(1)).toDto(platform);
    }
}
