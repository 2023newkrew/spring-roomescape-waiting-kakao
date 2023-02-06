package nextstep.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.persist.Schedule;
import nextstep.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 생성 API (관리자 권한 필요)")
    @PostMapping("/admin/schedules")
    public ResponseEntity scheduleSave(@RequestBody ScheduleRequest scheduleRequest) {
        Long id = scheduleService.addSchedule(scheduleRequest);
        return ResponseEntity.created(URI.create("/schedules/" + id)).build();
    }

    @Operation(summary = "테마 ID와 날짜로 스케줄 조회 API")
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> scheduleList(@RequestParam Long themeId, @RequestParam String date) {
        return ResponseEntity.ok().body(scheduleService.findAllByThemeIdAndDate(themeId, date));
    }

    @Operation(summary = "스케줄 삭제 API (관리자 권한 필요)")
    @DeleteMapping("/admin/schedules/{id}")
    public ResponseEntity scheduleRemove(@PathVariable Long id) {
        scheduleService.removeSchedule(id);

        return ResponseEntity.noContent().build();
    }
}
