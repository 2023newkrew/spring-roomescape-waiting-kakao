package nextstep.service;

import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ScheduleRequest;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Theme;
import nextstep.repository.ScheduleDao;
import nextstep.repository.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    @Transactional
    public Long addSchedule(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId());
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    @Transactional(readOnly = true)
    public List<Schedule> findAllByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    @Transactional
    public void removeSchedule(Long id) {
        scheduleDao.deleteById(id);
    }
}
