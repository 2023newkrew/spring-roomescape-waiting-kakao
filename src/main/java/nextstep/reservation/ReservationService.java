package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.revenue.Revenue;
import nextstep.revenue.RevenueDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final RevenueDao revenueDao;

    public ReservationService(ReservationDao reservationDao,
                              ThemeDao themeDao,
                              ScheduleDao scheduleDao,
                              MemberDao memberDao,
                              RevenueDao revenueDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.revenueDao = revenueDao;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Member member = memberDao.findById(userDetails.getId());
        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public void approve(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }
        if (reservation.getStatus() != ReservationStatus.UNAPPROVED) {
            throw new IllegalArgumentException("승인할 수 있는 상태가 아닙니다.");
        }
        reservationDao.updateStatusTo(id, ReservationStatus.APPROVED);
        revenueDao.save(new Revenue(reservation, reservation.getSchedule().getTheme().getPrice(), LocalDate.now()));
    }

    public void cancel(UserDetails member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        switch (reservation.getStatus()) {
            case UNAPPROVED:
                reservationDao.updateStatusTo(id, ReservationStatus.CANCELED);
                break;
            case APPROVED:
                reservationDao.updateStatusTo(id, ReservationStatus.CANCEL_WAITING);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void reject(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        reservationDao.updateStatusTo(id, ReservationStatus.REJECTED);
    }
    
}
