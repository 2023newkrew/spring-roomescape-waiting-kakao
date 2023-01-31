package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.support.NoEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import nextstep.waiting.Waiting;
import nextstep.waiting.WaitingDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final WaitingDao waitingDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao, WaitingDao waitingDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.waitingDao = waitingDao;
    }

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NoEntityException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NoEntityException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NoEntityException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);

        List<Waiting> waitings = waitingDao.findByScheduleId(reservation.getSchedule().getId());
        if (!waitings.isEmpty()) {
            Waiting waiting = waitings.get(0);
            Reservation nextReservation = new Reservation(waiting.getSchedule(), waiting.getMember());
            reservationDao.save(nextReservation);
            waitingDao.deleteById(waiting.getId());
        }
    }

    public List<ReservationResponse> findAllOfMember(UserDetails userDetails) {
        Member member = memberDao.findById(userDetails.getId());
        if (member == null) {
            throw new AuthenticationException();
        }

        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());
        return ReservationResponse.toList(reservations);
    }
}
