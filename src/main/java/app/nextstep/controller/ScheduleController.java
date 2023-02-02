package app.nextstep.controller;

import app.nextstep.domain.Schedule;
import app.nextstep.domain.Theme;
import app.nextstep.dto.ScheduleRequest;
import app.nextstep.service.ScheduleService;
import app.nextstep.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ScheduleController {
    private ScheduleService scheduleService;
    private ThemeService themeService;

    public ScheduleController(ScheduleService scheduleService, ThemeService themeService) {
        this.scheduleService = scheduleService;
        this.themeService = themeService;
    }

    @PostMapping("/admin/schedules")
    public ResponseEntity createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        Theme theme = themeService.findById(scheduleRequest.getThemeId());
        Long id = scheduleService.create(scheduleRequest.toSchedule(theme));
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }

    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
