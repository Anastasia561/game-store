package pl.edu.gamestore.game.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.edu.gamestore.game.GameServiceImpl;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.GameMapper;
import pl.edu.gamestore.game.Game;
import pl.edu.gamestore.game.GameRepository;
import pl.edu.gamestore.genre.Genre;
import pl.edu.gamestore.genre.GenreService;
import pl.edu.gamestore.platform.Platform;
import pl.edu.gamestore.platform.PlatformService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private GenreService genreService;
    @Mock
    private PlatformService platformService;
    @Mock
    private GameMapper gameMapper;
    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    void findAll_shouldReturnMappedPageOfGames() {
        GameFilterDto filter = new GameFilterDto("test", "test", "test");
        Pageable pageable = PageRequest.of(0, 2);

        Game game1 = new Game();
        Game game2 = new Game();

        GameResponseDto dto1 = new GameResponseDto(1L, "Test title", "desc",
                BigDecimal.valueOf(20.4), LocalDate.of(2026, 10, 2), "url",
                new HashSet<>(), new HashSet<>());
        GameResponseDto dto2 = new GameResponseDto(1L, "Test title", "desc",
                BigDecimal.valueOf(20.4), LocalDate.of(2026, 10, 2), "url",
                new HashSet<>(), new HashSet<>());

        Page<Game> gamePage = new PageImpl<>(List.of(game1, game2), pageable, 2);

        when(gameRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(gamePage);
        when(gameMapper.toDto(game1)).thenReturn(dto1);
        when(gameMapper.toDto(game2)).thenReturn(dto2);

        Page<GameResponseDto> result = gameService.findAll(filter, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));

        verify(gameRepository).findAll(any(Specification.class), eq(pageable));
        verify(gameMapper).toDto(game1);
        verify(gameMapper).toDto(game2);
    }

    @Test
    void findById_shouldReturnGameDto_whenGameExists() {
        Long id = 1L;
        Game game = new Game();
        GameResponseDto dto = new GameResponseDto(1L, "Test title", "desc",
                BigDecimal.valueOf(20.4), LocalDate.of(2026, 10, 2), "url",
                new HashSet<>(), new HashSet<>());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(gameMapper.toDto(game)).thenReturn(dto);

        GameResponseDto result = gameService.findById(id);

        assertNotNull(result);
        assertEquals(dto, result);

        verify(gameRepository).findById(id);
        verify(gameMapper).toDto(game);
    }

    @Test
    void findById_shouldThrowException_whenGameNotFound() {
        Long id = 1L;

        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> gameService.findById(id));

        verify(gameRepository).findById(id);
        verifyNoInteractions(gameMapper);
    }

    @Test
    void create_shouldCreateGameAndReturnId_whenInputIsValid() {
        GameRequestDto dto = new GameRequestDto("Witcher 3", "Des", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L, 2L), Set.of(10L));

        Game game = new Game();

        Genre g1 = new Genre();
        g1.setId(1L);
        Genre g2 = new Genre();
        g2.setId(2L);

        Platform p1 = new Platform();
        p1.setId(10L);

        when(gameMapper.toEntity(dto)).thenReturn(game);
        when(genreService.findAllByIds(dto.genreIds())).thenReturn(Set.of(g1, g2));
        when(platformService.findAllByIds(dto.platformIds())).thenReturn(Set.of(p1));

        Game savedGame = new Game();
        savedGame.setId(100L);

        when(gameRepository.save(game)).thenReturn(savedGame);

        Long result = gameService.create(dto);

        assertEquals(100L, result);

        assertEquals(2, game.getGenres().size());
        assertEquals(1, game.getPlatforms().size());

        verify(gameMapper).toEntity(dto);
        verify(genreService).findAllByIds(dto.genreIds());
        verify(platformService).findAllByIds(dto.platformIds());
        verify(gameRepository).save(game);
    }

    @Test
    void delete_shouldDeleteGame_whenIdExists() {
        Long id = 1L;
        when(gameRepository.existsById(id)).thenReturn(true);

        gameService.delete(id);

        verify(gameRepository).existsById(id);
        verify(gameRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrowException_whenGameNotFound() {
        Long id = 1L;
        when(gameRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> gameService.delete(id));

        verify(gameRepository).existsById(id);
        verify(gameRepository, never()).deleteById(any());
    }

    @Test
    void update_shouldUpdateGame_whenInputIsValid() {
        Long id = 1L;

        Game game = new Game();
        game.setGenres(new HashSet<>());
        game.setPlatforms(new HashSet<>());

        GameRequestDto dto = new GameRequestDto("Witcher 3", "Des", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L, 2L), Set.of(10L));

        Genre g1 = new Genre();
        g1.setId(1L);
        Genre g2 = new Genre();
        g2.setId(2L);

        Platform p1 = new Platform();
        p1.setId(10L);

        GameResponseDto response = new GameResponseDto(1L, "Test title", "desc",
                BigDecimal.valueOf(20.4), LocalDate.of(2026, 10, 2), "url",
                new HashSet<>(), new HashSet<>());

        when(gameRepository.findById(id)).thenReturn(Optional.of(game));
        when(genreService.findAllByIds(dto.genreIds())).thenReturn(Set.of(g1, g2));
        when(platformService.findAllByIds(dto.platformIds())).thenReturn(Set.of(p1));
        when(gameMapper.toDto(game)).thenReturn(response);

        GameResponseDto result = gameService.update(id, dto);

        assertEquals(response, result);

        assertEquals(2, game.getGenres().size());
        assertEquals(1, game.getPlatforms().size());

        verify(gameRepository).findById(id);
        verify(gameMapper).updateEntityFromDto(dto, game);
        verify(genreService).findAllByIds(dto.genreIds());
        verify(platformService).findAllByIds(dto.platformIds());
        verify(gameMapper).toDto(game);
    }

    @Test
    void update_shouldThrowException_whenGameNotFound() {
        Long id = 1L;
        GameRequestDto dto = new GameRequestDto("Witcher 3", "Des", BigDecimal.valueOf(20.4),
                LocalDate.of(2026, 10, 2), "url", Set.of(1L, 2L), Set.of(10L));

        when(gameRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> gameService.update(id, dto));
        assertEquals("Game not found", ex.getMessage());

        verifyNoInteractions(gameMapper, genreService, platformService);
    }
}
