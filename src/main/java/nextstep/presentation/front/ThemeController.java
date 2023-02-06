package nextstep.presentation.front;

import nextstep.dto.response.ThemeResponse;
import nextstep.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ThemeController {
    private ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> showThemes() {
        return ResponseEntity.ok(themeService.findAll());
    }

}
