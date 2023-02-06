package nextstep.presentation.admin;

import lombok.RequiredArgsConstructor;
import nextstep.dto.request.ScheduleRequest;
import nextstep.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/admin/schedules")
@RestController
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.create(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        scheduleService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
