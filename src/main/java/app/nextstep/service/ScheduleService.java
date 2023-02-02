package app.nextstep.service;

import app.nextstep.dao.ScheduleDao;
import app.nextstep.domain.Schedule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleService {
    private ScheduleDao scheduleDao;

    public ScheduleService(ScheduleDao scheduleDao) {
        this.scheduleDao = scheduleDao;
    }

    public Long create(Long themeId, LocalDate date, LocalTime time) {
        return scheduleDao.save(themeId, date, time);
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
