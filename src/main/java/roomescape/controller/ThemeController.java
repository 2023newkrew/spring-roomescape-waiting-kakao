package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.Admin;
import roomescape.controller.dto.ThemeControllerGetResponse;
import roomescape.controller.dto.ThemeControllerPostBody;
import roomescape.controller.dto.ThemeControllerPostResponse;
import roomescape.service.ThemeService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService service;

    @Admin
    @PostMapping(value = "/", produces = "application/json;charset=utf-8")
    public ResponseEntity<ThemeControllerPostResponse> createTheme(@Valid @RequestBody ThemeControllerPostBody body) {
        var id = service.createTheme(body);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/api/themes/%d", id))
                             .body(new ThemeControllerPostResponse(id));
    }

    @GetMapping(value = "/", produces = "application/json;charset=utf-8")
    public ResponseEntity<List<ThemeControllerGetResponse>> pageTheme(@RequestParam(defaultValue = "0") int page) {
        var themes = service.pageTheme(page);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(themes
                                     .map(v -> new ThemeControllerGetResponse(v.getId(), v.getName(), v.getDesc(), v.getPrice()))
                                     .collect(Collectors.toList())
                             );
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<ThemeControllerGetResponse> getTheme(@PathVariable long id) {
        var theme = service.getTheme(id);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ThemeControllerGetResponse(theme.getId(), theme.getName(), theme.getDesc(), theme.getPrice()));
    }

    @Admin
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteTheme(@PathVariable long id) {
        service.deleteTheme(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
