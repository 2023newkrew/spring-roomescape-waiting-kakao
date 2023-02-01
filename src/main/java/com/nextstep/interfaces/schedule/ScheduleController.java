package com.nextstep.interfaces.schedule;

import lombok.RequiredArgsConstructor;
import com.nextstep.interfaces.schedule.dtos.ScheduleRequest;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;
import com.nextstep.interfaces.schedule.dtos.ScheduleSearchRequest;
import com.nextstep.domains.schedule.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private static final String SCHEDULE_PATH = "/schedules/";

    private final ScheduleService service;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@RequestBody @Validated ScheduleRequest request) {
        ScheduleResponse schedule = service.create(request);
        URI location = URI.create(SCHEDULE_PATH + schedule.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable("schedule_id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponse>> search(@RequestBody @Validated ScheduleSearchRequest request) {
        String date = request.getDate();

        return ResponseEntity.ok(service.getByThemeIdAndDate(request.getThemeId(), LocalDate.parse(date)));
    }

    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("schedule_id") Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
