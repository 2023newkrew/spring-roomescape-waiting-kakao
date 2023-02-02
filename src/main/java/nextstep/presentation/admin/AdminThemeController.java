package nextstep.presentation.admin;

import lombok.RequiredArgsConstructor;
import nextstep.dto.request.ThemeRequest;
import nextstep.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/admin/themes")
@RestController
public class AdminThemeController {

    private final ThemeService themeService;

    @PostMapping("/themes")
    public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest themeRequest) {
        Long id = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
