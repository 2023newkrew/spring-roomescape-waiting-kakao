package nextstep.presentation.front;

import nextstep.dto.response.ScheduleResponse;
import nextstep.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponse>> showReservations(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findByThemeIdAndDate(themeId, date));
    }
}
