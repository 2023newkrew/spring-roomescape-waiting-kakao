package nextstep.schedule;

import java.util.List;
import java.util.Objects;
import nextstep.common.Lock;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import nextstep.reservation.service.ReservationService;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ScheduleService {
    private ScheduleDao scheduleDao;
    private ThemeDao themeDao;
    private ReservationDao reservationDao;
    private ReservationWaitingDao reservationWaitingDao;
    public static final Lock scheduleListLock = new Lock();

    public ScheduleService(ScheduleDao scheduleDao, ThemeDao themeDao, ReservationDao reservationDao,
                           ReservationWaitingDao reservationWaitingDao) {
        this.scheduleDao = scheduleDao;
        this.themeDao = themeDao;
        this.reservationDao = reservationDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(ScheduleRequest scheduleRequest) {
        Theme theme = themeDao.findById(scheduleRequest.getThemeId());
        return scheduleDao.save(scheduleRequest.toEntity(theme));
    }

    @Transactional(readOnly = true)
    public List<Schedule> findByThemeIdAndDate(Long themeId, String date) {
        return scheduleDao.findByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long id) {
        Schedule schedule = scheduleDao.findById(id);
        if (Objects.isNull(schedule)) {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        ReservationService.reservationListLock.lock();
        List<Reservation> reservationList = reservationDao.findAllByScheduleId(id);
        List<ReservationWaiting> reservationWaitingList = reservationWaitingDao.findByScheduleId(id);
        if (reservationList.size() > 0 || reservationWaitingList.size() > 0) {
            ReservationService.reservationListLock.unlock();
            throw new RoomReservationException(ErrorCode.SCHEDULE_CANT_BE_DELETED);
        }
        scheduleDao.deleteById(id);
        ReservationService.reservationListLock.unlock();
    }
}
