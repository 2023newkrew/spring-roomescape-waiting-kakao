package nextstep.schedule.service;

import nextstep.schedule.dto.ScheduleRequest;
import nextstep.schedule.dto.ScheduleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponse create(ScheduleRequest request);

    ScheduleResponse getById(Long id);

    List<ScheduleResponse> getByThemeIdAndDate(Long themeId, LocalDate date);

    boolean deleteById(Long id);
}
