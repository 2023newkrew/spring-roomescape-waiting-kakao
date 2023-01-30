package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
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
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public Long createWaiting(Member member, Long scheduleId) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(scheduleId);
        if (schedule == null) {
            throw new NullPointerException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> getReservationsByMember(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        List<Reservation> reservations = reservationDao.findByMemberId(member.getId());
        List<Reservation> reservationsNotWaiting = reservations.stream()
                .map(Reservation::getSchedule)
                .map(Schedule::getId)
                .map(reservationDao::findByScheduleId)
                .map(reservationsBySchedule -> reservationsBySchedule.stream()
                        .min(Comparator.comparing(Reservation::getWaitNumber))
                        .orElseThrow())
                .collect(Collectors.toList());

        List<Reservation> result = new ArrayList<>();
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getWaitNumber() == reservationsNotWaiting.get(i).getWaitNumber()) {
                result.add(reservations.get(i));
            }
        }
        return result;
    }
}
