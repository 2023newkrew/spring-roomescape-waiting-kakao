package nextstep.schedule;

import nextstep.exception.ErrorCode;
import nextstep.exception.RoomEscapeException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;

    public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
    }

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId())
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS));
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<ScheduleResponse> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (!scheduleDao.deleteById(id)) {
            throw new RoomEscapeException(ErrorCode.ENTITY_NOT_EXISTS);
        }
    }
}
