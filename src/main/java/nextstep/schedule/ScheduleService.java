package nextstep.schedule;

import lombok.RequiredArgsConstructor;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId())
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.THEME_NOT_FOUND));
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
