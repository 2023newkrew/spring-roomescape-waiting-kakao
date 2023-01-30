package nextstep.schedule.controller;

import auth.domain.RoleType;
import auth.support.AuthorizationRequired;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.model.Schedule;
import nextstep.schedule.model.ScheduleRequest;
import nextstep.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }

    @AuthorizationRequired(RoleType.ADMIN)
    @PostMapping("/admin/schedules")
    public ResponseEntity<Void> createSchedule(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @AuthorizationRequired(RoleType.ADMIN)
    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
