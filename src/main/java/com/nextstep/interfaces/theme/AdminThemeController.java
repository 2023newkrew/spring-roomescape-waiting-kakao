package com.nextstep.interfaces.theme;

import com.nextstep.interfaces.theme.dtos.ThemeRequest;
import com.nextstep.interfaces.theme.dtos.ThemeResponse;
import lombok.RequiredArgsConstructor;
import com.nextstep.domains.theme.ThemeService;
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


    @PostMapping
    public ResponseEntity<ThemeResponse> create(@RequestBody @Validated ThemeRequest request) {
        ThemeResponse theme = service.create(request);
        URI location = URI.create(THEME_PATH + theme.getId());

        return ResponseEntity.created(location)
                .build();
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

