package nextstep.theme.controller;

import lombok.RequiredArgsConstructor;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import nextstep.theme.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/themes")
@RestController
public class ThemeController {

    private static final String THEME_PATH = "/themes/";

    private final ThemeService service;

    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody @Validated ThemeRequest request) {
        ThemeResponse theme = service.create(request);
        URI location = URI.create(THEME_PATH + theme.getId());

        return ResponseEntity.created(location)
                .build();
    }

    @GetMapping("/{theme_id}")
    public ResponseEntity<ThemeResponse> getById(@PathVariable("theme_id") Long themeId) {

        return ResponseEntity.ok(service.getById(themeId));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{theme_id}")
    public ResponseEntity<ThemeResponse> update(
            @PathVariable("theme_id") Long themeId,
            @RequestBody @Validated ThemeRequest request) {

        return ResponseEntity.ok(service.update(themeId, request));
    }

    @DeleteMapping("/{theme_id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("theme_id") Long themeId) {

        return ResponseEntity.ok(service.deleteById(themeId));
    }
}
