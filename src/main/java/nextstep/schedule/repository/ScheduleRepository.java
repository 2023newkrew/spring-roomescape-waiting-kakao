package nextstep.schedule.repository;

import nextstep.schedule.domain.Schedule;

import java.sql.Date;
import java.util.List;

public interface ScheduleRepository {

    Schedule insert(Schedule schedule);

    Schedule getById(Long id);

    List<Schedule> getByThemeIdAndDate(Long themeId, Date date);

    boolean deleteById(Long id);
}
