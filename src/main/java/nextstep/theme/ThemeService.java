package nextstep.theme;

import java.util.List;
import java.util.Objects;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import org.springframework.stereotype.Service;
import nextstep.schedule.ScheduleService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ThemeService {
    private ThemeDao themeDao;
    private ScheduleDao scheduleDao;

    public ThemeService(ThemeDao themeDao, ScheduleDao scheduleDao) {
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
    }

    public Long create(ThemeRequest themeRequest) {
        return themeDao.save(themeRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public List<Theme> findAll() {
        return themeDao.findAll();
    }

    public void delete(Long id) {
        Theme theme = themeDao.findById(id).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.THEME_NOT_FOUND);
        });
        ScheduleService.scheduleListLock.lock();
        List<Schedule> schedules = scheduleDao.findByThemeId(id);
        if (schedules.size() > 0) {
            ScheduleService.scheduleListLock.unlock();
            throw new RoomReservationException(ErrorCode.THEME_CANT_BE_DELETED);
        }
        themeDao.delete(id);
        ScheduleService.scheduleListLock.unlock();
    }
}
