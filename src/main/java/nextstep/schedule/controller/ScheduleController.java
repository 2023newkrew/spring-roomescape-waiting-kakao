package nextstep.schedule.controller;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.dto.ScheduleSearchRequest;
import nextstep.schedule.mapper.ScheduleMapper;
import nextstep.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService service;

    private final ScheduleMapper mapper;

    @GetMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable("schedule_id") Long id) {
        Schedule schedule = service.getById(id);

        return ResponseEntity.ok(mapper.toResponse(schedule));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponse>> search(@RequestBody @Validated ScheduleSearchRequest request) {
        Schedule schedule = mapper.fromRequest(request);
        List<Schedule> schedules = service.getAllByThemeAndDate(schedule.getTheme(), schedule.getDate());

        return ResponseEntity.ok(mapper.toResponses(schedules));
    }

}
