package pl.edu.gamestore.platform;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.gamestore.platform.dto.PlatformResponseDto;
import pl.edu.gamestore.wrapper.ResponseWrapper;

import java.util.List;

@RestController
@RequestMapping("/platforms")
@RequiredArgsConstructor
public class PlatformController {
    private final PlatformService platformService;

    @GetMapping
    public ResponseWrapper<List<PlatformResponseDto>> findAll() {
        return ResponseWrapper.ok(platformService.findAll());
    }
}
