package nextstep.schedule.service;

import nextstep.schedule.domain.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    Schedule create(Schedule schedule);

    Schedule getById(Long id);

    List<Schedule> getByThemeIdAndDate(Long themeId, LocalDate date);

    boolean deleteById(Long id);
}
