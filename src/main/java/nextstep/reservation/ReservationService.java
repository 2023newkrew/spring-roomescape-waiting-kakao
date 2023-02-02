package nextstep.reservation;

import auth.exception.AuthErrorCode;
import auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import nextstep.exception.business.BusinessErrorCode;
import nextstep.exception.business.BusinessException;
import nextstep.exception.dataaccess.DataAccessErrorCode;
import nextstep.exception.dataaccess.DataAccessException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.revenue.Revenue;
import nextstep.revenue.RevenueDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    private final RevenueDao revenueDao;

    public Reservation create(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.SCHEDULE_NOT_FOUND));

        List<Reservation> reservations = reservationDao.findAllByScheduleId(schedule.getId());
        if (!reservations.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.RESERVATION_ALREADY_EXIST_AT_THAT_TIME);
        }

        Reservation newReservation = new Reservation(schedule, member, Reservation.Status.WAITING_APPROVAL);

        return new Reservation(reservationDao.save(newReservation), schedule, member, 0L, Reservation.Status.WAITING_APPROVAL);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Optional<Theme> theme = themeDao.findById(themeId);
        if (theme.isEmpty()) {
            throw new DataAccessException(DataAccessErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void cancel(Member member, Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.isSameMember(member)) {
            throw new BusinessException(BusinessErrorCode.DELETE_FAILED_WHEN_NOT_MY_RESERVATION);
        }

        if (reservation.getStatus() == Reservation.Status.WAITING_APPROVAL) {
            reservationDao.deleteById(reservationId);
        } else if (reservation.getStatus() == Reservation.Status.APPROVAL) {
            reservation.changeStatus(Reservation.Status.CANCEL_WAITING);
            reservationDao.save(reservation);
        }
    }

    public List<Reservation> getReservationsByMember(Member member) {
        if (member == null) {
            throw new AuthException(AuthErrorCode.INVALID_USER);
        }
        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());
        return reservations.stream()
                .filter(reservation -> {
                    Long scheduleId = reservation.getSchedule().getId();
                    Long waitTicketNumber = reservation.getWaitTicketNumber();
                    return isReservationNotWaiting(reservationDao.getPriority(scheduleId, waitTicketNumber));
                })
                .collect(Collectors.toList());
    }

    private static boolean isReservationNotWaiting(Long waitNum) {
        return waitNum == 0;
    }

    public void approve(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new DataAccessException(DataAccessErrorCode.RESERVATION_NOT_FOUND));

        if (isWaitingReservation(reservation)) {
            throw new BusinessException(BusinessErrorCode.RESERVATION_WAITING_CANNOT_APPROVE);
        }

        reservation.changeStatus(Reservation.Status.APPROVAL);
        Revenue revenueLog = new Revenue(reservation.getId(), reservation.getSchedule().getTheme().getPrice());

        reservationDao.save(reservation);
        revenueDao.save(revenueLog);
    }

    private boolean isWaitingReservation(Reservation reservation) {
        Long scheduleId = reservation.getSchedule().getId();
        Long waitTicketNumber = reservation.getWaitTicketNumber();
        Long waitNum = reservationDao.getPriority(scheduleId, waitTicketNumber);
        return !isReservationNotWaiting(waitNum);
    }
}
