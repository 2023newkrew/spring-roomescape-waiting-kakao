package nextstep.schedule.controller;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.mapper.ScheduleMapper;
import nextstep.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/admin/schedules")
@RestController
public class AdminScheduleController {

    private static final String SCHEDULE_PATH = "/schedules/";

    private final ScheduleService service;

    private final ScheduleMapper mapper;

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(@RequestBody @Validated ScheduleRequest request) {
        ScheduleEntity schedule = service.create(mapper.fromRequest(request));
        URI location = URI.create(SCHEDULE_PATH + schedule.getId());

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("schedule_id") Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
