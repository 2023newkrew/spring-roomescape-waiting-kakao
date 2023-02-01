package roomwaiting.nextstep.schedule;

import roomwaiting.nextstep.reservation.dao.ReservationDao;
import roomwaiting.nextstep.theme.Theme;
import roomwaiting.nextstep.theme.ThemeDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

import static roomwaiting.support.Messages.*;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;
    private final ReservationDao reservationDao;

    public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao, ReservationDao reservationDao) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
    }
    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId()).orElseThrow(() ->
                new NullPointerException(THEME_NOT_FOUND.getMessage() + ID + scheduleRequest.getThemeId()));
        if (scheduleDao.isExistsByTimeAndDate(scheduleRequest.getTime(), scheduleRequest.getDate())) {
            throw new DuplicateKeyException(ALREADY_REGISTERED_SCHEDULE.getMessage());
        }
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        reservationDao.findByScheduleId(id).ifPresent(val -> {
                    throw new DuplicateKeyException(ALREADY_REGISTERED_RESERVATION.getMessage());
                });
        scheduleDao.findById(id).orElseThrow(() ->
                new NullPointerException(SCHEDULE_NOT_FOUND.getMessage() + ID + id));
        scheduleDao.deleteById(id);
    }
}
