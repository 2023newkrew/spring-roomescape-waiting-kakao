package nextstep.theme.controller;

import auth.domain.MemberRoleType;
import auth.support.AuthorizationRequired;
import lombok.RequiredArgsConstructor;
import nextstep.theme.model.Theme;
import nextstep.theme.model.ThemeRequest;
import nextstep.theme.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> showThemes() {
        List<Theme> results = themeService.findAll();
        return ResponseEntity.ok().body(results);
    }

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @PostMapping("/admin/themes")
    public ResponseEntity<Void> createTheme(@Valid @RequestBody ThemeRequest themeRequest) {
        Long id = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
