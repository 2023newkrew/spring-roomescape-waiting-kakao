package com.nextstep.domains.theme;

import com.nextstep.domains.theme.dtos.ThemeRequest;
import com.nextstep.domains.theme.entities.ThemeEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<Void> createTheme(@RequestBody ThemeRequest themeRequest) {
        Long id = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeEntity>> showThemes() {
        List<ThemeEntity> results = themeService.findAll();
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
