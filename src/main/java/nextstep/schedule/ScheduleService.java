package nextstep.schedule;

import static nextstep.exception.ErrorMessage.NOT_EXIST_THEME;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    public Long create(final ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId())
            .orElseThrow(() -> new NullPointerException(NOT_EXIST_THEME.getMessage()));

        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<Schedule> findByThemeIdAndDate(final Long themeId, final String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(final Long id) {
        scheduleDao.deleteById(id);
    }
}
