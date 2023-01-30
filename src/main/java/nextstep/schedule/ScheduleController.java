package nextstep.schedule;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Validated
@RestController
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/admin/schedules")
    public ResponseEntity createSchedule(@RequestBody @Valid ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> showReservations(@RequestParam @NotNull @Min(1L) Long themeId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }

    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity deleteReservation(@PathVariable @NotNull @Min(1L) Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
