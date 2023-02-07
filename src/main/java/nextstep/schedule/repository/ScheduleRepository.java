package nextstep.schedule.repository;

import nextstep.schedule.domain.Schedule;
import nextstep.theme.domain.Theme;

import java.sql.Date;
import java.util.List;

public interface ScheduleRepository {

    Schedule insert(Schedule schedule);

    Schedule getById(Long id);

    List<Schedule> getAllByThemeAndDate(Theme theme, Date date);

    boolean deleteById(Long id);
}
