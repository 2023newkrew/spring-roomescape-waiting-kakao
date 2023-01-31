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
public class ReservationWaitingService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public ReservationWaitingService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
    }

    public Long create(String username, ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberDao.findByUsername(username);
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationWaitingRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        return reservationDao.save(newReservation);
    }

    public List<ReservationWaiting> findAllByUsername(String username) {
        return reservationDao.findAllByUsername(username)
                .stream()
                .map(e -> {
                    return new ReservationWaiting(e, reservationDao.getWaitingNumber(e.getSchedule().getId(), e.getId()));
                })
                .collect(Collectors.toList());
    }

    public Long getWaitingNum(ReservationWaitingRequest reservationWaitingRequest, Long id) {
        return reservationDao.getWaitingNumber(reservationWaitingRequest.getScheduleId(), id);
    }
}
