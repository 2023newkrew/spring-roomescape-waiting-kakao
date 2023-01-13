package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ThemeControllerGetResponse;
import roomescape.dto.ThemeControllerPostBody;
import roomescape.dto.ThemeControllerPostResponse;
import roomescape.exception.NotExistThemeException;
import roomescape.repository.ThemeRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeRepository repository;

    public ThemeController(ThemeRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<ThemeControllerPostResponse> createTheme(@Valid @RequestBody ThemeControllerPostBody body) {
        var id = repository.insert(body.getName(), body.getDesc(), body.getPrice());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/themes/%d", id))
                             .body(new ThemeControllerPostResponse(id));
    }

    @GetMapping(value = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<List<ThemeControllerGetResponse>> pageTheme(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(repository.selectPage(30, Math.max(page, 0))
                                             .stream()
                                             .map(v -> new ThemeControllerGetResponse(v.getId(), v.getName(), v.getDesc(), v.getPrice()))
                                             .collect(Collectors.toList())
                             );
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<ThemeControllerGetResponse> getTheme(@PathVariable long id) {
        var target = repository.selectById(id)
                               .map(v -> new ThemeControllerGetResponse(v.getId(), v.getName(), v.getDesc(), v.getPrice()));
        if (target.isEmpty()) {
            throw new NotExistThemeException(id);
        }
        return ResponseEntity.status(HttpStatus.OK)
                             .body(target.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteTheme(@PathVariable long id) {
        var affectedRows = repository.delete(id);
        if (affectedRows == 0) {
            throw new NotExistThemeException(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
