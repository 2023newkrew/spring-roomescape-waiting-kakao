package nextstep.schedule;

import nextstep.reservationwaitings.ReservationWaitingNumGenerator;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleDao scheduleDao;
    private final ThemeDao themeDao;
    private final ReservationWaitingNumGenerator reservationWaitingNumGenerator;

    public ScheduleService(ScheduleDao scheduleDao,
                           ThemeDao themeDao,
                           ReservationWaitingNumGenerator reservationWaitingNumGenerator) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
        this.reservationWaitingNumGenerator = reservationWaitingNumGenerator;
    }

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId());
        Long scheduleId = scheduleDao.save(scheduleRequest.toEntity(theme));
        reservationWaitingNumGenerator.createWaitNum(scheduleId);
        return scheduleId;
    }

    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        scheduleDao.deleteById(id);
    }
}
