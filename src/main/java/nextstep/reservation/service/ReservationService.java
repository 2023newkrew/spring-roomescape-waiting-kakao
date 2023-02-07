package nextstep.reservation.service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;
    public static final AtomicInteger reservationListLock = new AtomicInteger(0);

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao,
                              ReservationWaitingDao reservationWaitingDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (Objects.isNull(member)) {
            throw new RoomReservationException(ErrorCode.AUTHENTICATION_REQUIRED);
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (Objects.isNull(schedule)) {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        while(!reservationListLock.compareAndSet(0, 1)) {}
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            reservationListLock.set(0);
            throw new RoomReservationException(ErrorCode.DUPLICATE_RESERVATION);
        }
        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        long savedId = reservationDao.save(newReservation);
        reservationListLock.set(0);
        return savedId;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (Objects.isNull(theme)) {
            throw new RoomReservationException(ErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (Objects.isNull(reservation)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        if (!reservation.sameMember(member)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationDao.deleteById(id);
        ReservationWaiting reservationWaiting = reservationWaitingDao
                .findFirstByScheduleId(
                        reservation.getSchedule().getId()
                );
        if (Objects.isNull(reservationWaiting)) {
            return;
        }
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        reservationDao.save(reservationWaiting.convertToReservation());
    }

    @Transactional(readOnly = true)
    public List<Reservation> lookUp(Member member) {
        return reservationDao.findByMemberId(member.getId());
    }
}
