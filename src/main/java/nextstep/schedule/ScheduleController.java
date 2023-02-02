package nextstep.schedule;

import auth.NeedAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @NeedAuth(role = NeedAuth.ADMIN)
    @PostMapping("/admin/schedules")
    public ResponseEntity createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }

    @NeedAuth(role = NeedAuth.ADMIN)
    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
