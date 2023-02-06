package nextstep.schedule.service;

import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    Schedule create(Schedule schedule);

    Schedule getById(Long id);

    List<Schedule> getAllByThemeAndDate(Theme theme, LocalDate date);

    boolean deleteById(Long id);
}
