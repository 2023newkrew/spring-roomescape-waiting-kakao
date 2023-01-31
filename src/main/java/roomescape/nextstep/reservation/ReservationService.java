package roomescape.nextstep.reservation;

import roomescape.auth.AuthenticationException;
import roomescape.nextstep.member.Member;
import roomescape.nextstep.member.MemberDao;
import roomescape.nextstep.schedule.Schedule;
import roomescape.nextstep.schedule.ScheduleDao;
import roomescape.nextstep.support.DuplicateEntityException;
import roomescape.nextstep.theme.Theme;
import roomescape.nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

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

    public Reservation create(String username, ReservationRequest reservationRequest) {
        Member member = memberDao.findByUsername(username);
        ReservationStatus status = ReservationStatus.CONFIRMED;
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            status = ReservationStatus.WAITING;
        }

        Reservation newReservation = new Reservation(
                schedule,
                member,
                status
        );

        return reservationDao.save(newReservation);
    }

    private List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<Reservation> findFilteredReservationsByThemeIdAndDate(Long themeId, String date, ReservationStatus status) {
        return findAllByThemeIdAndDate(themeId, date).stream()
                .filter(e -> e.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void deleteById(String username, Long id) {
        Reservation reservation = reservationDao.findById(id);
        Member member = memberDao.findByUsername(username);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationWaiting> findAllByUsername(String username) {
        return reservationDao.findAllByUsername(username)
                .stream()
                .map(e -> {
                    return new ReservationWaiting(e, reservationDao.getWaitingNumber(e.getSchedule().getId(), e.getId()));
                })
                .collect(Collectors.toList());
    }

    public Long getWaitingNum(ReservationRequest reservationRequest, Long id) {
        return reservationDao.getWaitingNumber(reservationRequest.getScheduleId(), id);
    }

}
