package pl.edu.gamestore.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.gamestore.genre.dto.GenreResponseDto;
import pl.edu.gamestore.wrapper.ResponseWrapper;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseWrapper<List<GenreResponseDto>> getAllGenres() {
        return ResponseWrapper.ok(genreService.getAll());
    }
}
