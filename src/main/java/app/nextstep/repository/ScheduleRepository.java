package app.nextstep.repository;

import app.nextstep.dao.ScheduleDao;
import app.nextstep.domain.Schedule;
import app.nextstep.entity.ScheduleEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ScheduleRepository {
    private ScheduleDao scheduleDao;

    public ScheduleRepository(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, LocalDate date) {
        List<Schedule> schedules = new ArrayList<>();
        for (ScheduleEntity scheduleEntity : scheduleDao.findByThemeIdAndDate(themeId, Date.valueOf(date))) {
            schedules.add(scheduleEntity.toSchedule());
        }
        return schedules;
    }

    public Long save(Schedule schedule) {
        return scheduleDao.save(
                schedule.getTheme().getId(),
                Date.valueOf(schedule.getDate()),
                Time.valueOf(schedule.getTime()));
    }

    public void delete(Long id) {
        scheduleDao.deleteById(id);
    }
}
