package nextstep.theme.controller;

import lombok.RequiredArgsConstructor;
import nextstep.theme.domain.ThemeEntity;
import nextstep.theme.dto.ThemeRequest;
import nextstep.theme.dto.ThemeResponse;
import nextstep.theme.mapper.ThemeMapper;
import nextstep.theme.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/admin/themes")
@RestController
public class AdminThemeController {

    private static final String THEME_PATH = "/themes/";

    private final ThemeService service;
    private final ThemeMapper mapper;


    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody @Validated ThemeRequest request) {
        ThemeEntity theme = service.create(mapper.fromRequest(request));
        URI location = URI.create(THEME_PATH + theme.getId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{theme_id}")
    public ResponseEntity<ThemeResponse> update(
            @PathVariable("theme_id") Long themeId,
            @RequestBody @Validated ThemeRequest request) {
        ThemeEntity theme = service.update(themeId, mapper.fromRequest(request));

        return ResponseEntity.ok(mapper.toResponse(theme));
    }

    @DeleteMapping("/{theme_id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("theme_id") Long themeId) {
        return ResponseEntity.ok(service.deleteById(themeId));
    }
}

