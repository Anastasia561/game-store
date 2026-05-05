package pl.edu.gamestore.game.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.gamestore.game.dto.GameRequestDto;
import pl.edu.gamestore.game.dto.GameFilterDto;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.service.GameService;
import pl.edu.gamestore.wrapper.ResponseWrapper;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
@Tag(name = "Games", description = "Endpoints for managing games")
public class GameController {
    private final GameService gameService;

    @Operation(summary = "Endpoint for selecting page of games with optional filtering by title, genre or platform")
    @GetMapping
    public ResponseWrapper<Page<GameResponseDto>> findAll(GameFilterDto filter, Pageable pageable) {
        return ResponseWrapper.ok(gameService.findAll(filter, pageable));
    }

    @Operation(summary = "Endpoint for finding game by id")
    @GetMapping("/{id}")
    public ResponseWrapper<GameResponseDto> findById(@PathVariable Long id) {
        return ResponseWrapper.ok(gameService.findById(id));
    }

    @Operation(summary = "Endpoint for creating new game")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseWrapper<Long> create(@Valid @RequestBody GameRequestDto dto) {
        return ResponseWrapper.withStatus(HttpStatus.CREATED, gameService.create(dto));
    }

    @Operation(summary = "Endpoint fo deleting game by id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gameService.delete(id);
    }

    @Operation(summary = "Endpoint for updating game by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseWrapper<GameResponseDto> update(@PathVariable Long id,
                                                   @Valid @RequestBody GameRequestDto dto) {
        return ResponseWrapper.ok(gameService.update(id, dto));
    }
}
