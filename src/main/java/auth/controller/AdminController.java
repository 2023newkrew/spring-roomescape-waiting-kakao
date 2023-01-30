package auth.controller;

import lombok.RequiredArgsConstructor;
import auth.domain.MemberRoleType;
import auth.support.AuthorizationRequired;
import nextstep.schedule.model.ScheduleRequest;
import nextstep.schedule.service.ScheduleService;
import nextstep.theme.model.ThemeRequest;
import nextstep.theme.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ThemeService themeService;
    private final ScheduleService scheduleService;

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @PostMapping("/themes")
    public ResponseEntity<Void> createTheme(@Valid @RequestBody ThemeRequest themeRequest) {
        Long id = themeService.create(themeRequest);
        return ResponseEntity.created(URI.create("/themes/" + id)).build();
    }

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @PostMapping("/schedules")
    public ResponseEntity<Void> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @AuthorizationRequired(MemberRoleType.ADMIN)
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
