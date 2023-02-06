package nextstep.theme.controller;

import lombok.RequiredArgsConstructor;
import nextstep.theme.domain.Theme;
import nextstep.theme.dto.ThemeResponse;
import nextstep.theme.mapper.ThemeMapper;
import nextstep.theme.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/themes")
@RestController
public class ThemeController {

    private final ThemeService service;
    private final ThemeMapper mapper;


    @GetMapping("/{theme_id}")
    public ResponseEntity<ThemeResponse> getById(@PathVariable("theme_id") Long themeId) {
        Theme theme = service.getById(themeId);

        return ResponseEntity.ok(mapper.toResponse(theme));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getAll() {
        List<ThemeResponse> themes = getAllThemes();

        return ResponseEntity.ok(themes);
    }

    private List<ThemeResponse> getAllThemes() {
        return service.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
