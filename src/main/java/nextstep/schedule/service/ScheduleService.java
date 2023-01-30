package nextstep.schedule.service;

import lombok.RequiredArgsConstructor;
import nextstep.exception.NotExistEntityException;
import nextstep.schedule.dao.ScheduleDao;
import nextstep.schedule.model.Schedule;
import nextstep.schedule.model.ScheduleRequest;
import nextstep.theme.model.Theme;
import nextstep.theme.dao.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId())
                .orElseThrow(NotExistEntityException::new);
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
