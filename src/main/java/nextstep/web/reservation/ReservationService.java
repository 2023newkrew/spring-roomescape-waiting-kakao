package nextstep.web.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.exception.AuthErrorCode;
import nextstep.exception.BusinessException;
import nextstep.exception.CommonErrorCode;
import nextstep.web.member.Member;
import nextstep.web.reservation_waiting.ReservationWaiting;
import nextstep.web.reservation_waiting.ReservationWaitingDao;
import nextstep.web.schedule.Schedule;
import nextstep.web.schedule.ScheduleDao;
import nextstep.web.theme.Theme;
import nextstep.web.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new BusinessException(CommonErrorCode.DUPLICATE_ENTITY);
        }

        return reservationDao.save(new Reservation(schedule, member));
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new BusinessException(CommonErrorCode.NOT_EXIST_ENTITY);
        }
        if (!reservation.sameMember(member)) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED_DELETE);
        }
        convertReservationWaiting(reservation);

        reservationDao.deleteById(id);
    }

    private void convertReservationWaiting(Reservation reservation) {
        List<ReservationWaiting> reservationWaitings = reservationWaitingDao.findAllByScheduleIdOrderByDesc(reservation.getSchedule().getId());
        if (!reservationWaitings.isEmpty()) {
            ReservationWaiting reservationWaiting = reservationWaitings.get(reservationWaitings.size() - 1);
            Reservation toSave = Reservation.fromReservationWaiting(reservationWaiting);
            reservationDao.save(toSave);
            reservationWaitingDao.deleteById(reservationWaiting.getId());
        }
    }

    public List<Reservation> findAllByMember(Member member) {
        return reservationDao.findAllByMember(member);
    }
}
