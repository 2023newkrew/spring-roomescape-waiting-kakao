package nextstep.reservationwaiting;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationWaitingService {
    public final ReservationWaitingDao reservationWaitingDao;
    public final ScheduleDao scheduleDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationWaitingService(ReservationWaitingDao reservationWaitingDao, ScheduleDao scheduleDao, ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationWaitingDao = reservationWaitingDao;
        this.scheduleDao = scheduleDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public Long create(Long memberId, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findById(memberId);
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservations = reservationDao.findByScheduleId(schedule.getId());
        if (reservations.isEmpty()) {
            Reservation reservation = new Reservation(schedule, member);
            reservationDao.save(reservation);
        }

        int waitNum = reservationWaitingDao.getWaitNumByScheduleId(schedule.getId()) + 1;

        ReservationWaiting newReservationWaiting = new ReservationWaiting(
                schedule.getId(),
                member.getId(),
                waitNum
        );

        return reservationWaitingDao.save(newReservationWaiting);
    }
//
//    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
//        Theme theme = themeDao.findById(themeId);
//        if (theme == null) {
//            throw new NullPointerException();
//        }
//
//        return reservationDao.findAllByThemeIdAndDate(themeId, date);
//    }
//
//    public void deleteById(Member member, Long id) {
//        Reservation reservation = reservationDao.findById(id);
//        if (reservation == null) {
//            throw new NullPointerException();
//        }
//
//        if (!reservation.sameMember(member)) {
//            throw new AuthenticationException();
//        }
//
//        reservationDao.deleteById(id);
//    }
}
