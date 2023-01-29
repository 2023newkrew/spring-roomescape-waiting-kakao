package nextstep.service;

import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.ScheduleDao;
import nextstep.dto.request.ScheduleRequest;
import nextstep.domain.theme.Theme;
import nextstep.error.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.error.ErrorType.SCHEDULE_NOT_FOUND;

@Service
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final ThemeService themeService;

    public ScheduleService(ScheduleDao scheduleDao, ThemeService themeService) {
        this.scheduleDao = scheduleDao;
        this.themeService = themeService;
    }

    @Transactional
    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeService.findById(scheduleRequest.getThemeId());
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    @Transactional(readOnly = true)
    public Schedule findById(Long scheduleId) {
        return scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new ApplicationException(SCHEDULE_NOT_FOUND));
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
