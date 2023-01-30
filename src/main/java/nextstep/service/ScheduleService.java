package nextstep.service;

import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.persist.Schedule;
import nextstep.repository.ScheduleDao;
import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleService {
    private ScheduleDao scheduleDao;
    private ThemeDao themeDao;

    public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
    }

    @Transactional
    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId());
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    @Transactional(readOnly = true)
    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    @Transactional
    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
