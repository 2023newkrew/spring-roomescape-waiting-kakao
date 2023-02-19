package app.nextstep.controller;

import app.nextstep.domain.Schedule;
import app.nextstep.dto.ScheduleRequest;
import app.nextstep.dto.ScheduleResponse;
import app.nextstep.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/admin/schedules")
    public ResponseEntity createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest.toSchedule());
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @GetMapping(value = "/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ScheduleResponse>> getReservations(
            @RequestParam Long themeId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Schedule> schedules = scheduleService.findByThemeIdAndDate(themeId, date);
        List<ScheduleResponse> responseBody = new ArrayList<>();
        for (Schedule schedule : schedules) {
            responseBody.add(new ScheduleResponse(schedule));
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
