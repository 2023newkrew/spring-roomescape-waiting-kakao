package nextstep.service;

import nextstep.domain.dto.ScheduleRequest;
import nextstep.domain.persist.Schedule;
import nextstep.repository.ScheduleDao;
import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
    }

    public long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId());
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<Schedule> findByThemeIdAndDate(long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(long id) {
        scheduleDao.deleteById(id);
    }
}
