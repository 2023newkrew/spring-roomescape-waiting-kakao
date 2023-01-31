package nextstep.schedule.controller;

import lombok.RequiredArgsConstructor;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.schedule.dto.ScheduleSearchRequest;
import nextstep.schedule.mapper.ScheduleMapper;
import nextstep.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController {

    private final ScheduleService service;

    private final ScheduleMapper mapper;

    @GetMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponse> getById(@PathVariable("schedule_id") Long id) {
        ScheduleEntity schedule = service.getById(id);

        return ResponseEntity.ok(mapper.toResponse(schedule));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponse>> search(@RequestBody @Validated ScheduleSearchRequest request) {
        String date = request.getDate();

        return ResponseEntity.ok(getSchedules(request.getThemeId(), LocalDate.parse(date)));
    }

    private List<ScheduleResponse> getSchedules(Long themeId, LocalDate date) {
        return service.getByThemeIdAndDate(themeId, date)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
