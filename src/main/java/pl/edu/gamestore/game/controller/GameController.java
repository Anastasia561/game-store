package pl.edu.gamestore.game.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.gamestore.game.dto.GameResponseDto;
import pl.edu.gamestore.game.service.GameService;
import pl.edu.gamestore.wrapper.ResponseWrapper;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @GetMapping
    public ResponseWrapper<Page<GameResponseDto>> findAll(Pageable pageable) {
        return ResponseWrapper.ok(gameService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseWrapper<GameResponseDto> findById(@PathVariable Long id) {
        return ResponseWrapper.ok(gameService.findById(id));
    }
}
