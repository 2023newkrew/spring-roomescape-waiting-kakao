package app.nextstep.controller;

import app.nextstep.domain.Theme;
import app.nextstep.dto.ThemeRequest;
import app.nextstep.dto.ThemeResponse;
import app.nextstep.service.ThemeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ThemeController {
    private ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest themeRequest) {
        Long id = themeService.create(themeRequest.toTheme());
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @GetMapping(value = "/themes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<Theme> themes = themeService.findAll();
        List<ThemeResponse> responseBody = new ArrayList<>();
        for (Theme theme : themes) {
            responseBody.add(new ThemeResponse(theme));
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
