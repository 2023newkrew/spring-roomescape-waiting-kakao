package nextstep.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ThemeRequest;
import nextstep.domain.dto.response.ThemeResponse;
import nextstep.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @Operation(summary = "테마 생성 API (관리자 권한 필요)")
    @PostMapping("/admin/themes")
    public ResponseEntity<Void> themeSave(@RequestBody ThemeRequest themeRequest) {
        Long id = themeService.addTheme(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @Operation(summary = "모든 테마 조회 API")
    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> themeList() {
        List<ThemeResponse> results = themeService.findAll();
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "테마 삭제 API (관리자 권한 필요)")
    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> themeRemove(@PathVariable Long id) {
        themeService.removeTheme(id);

        return ResponseEntity.noContent().build();
    }
}
