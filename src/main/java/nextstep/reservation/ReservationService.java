package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.exception.NotExistEntityException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
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

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findMyReservations(Member member) {
        try {
            return reservationDao.findAllByMemberId(member.getId()).stream()
                    .map(r -> new ReservationResponse(r.getId(), r.getSchedule()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId)
                .orElseThrow(() -> new NotExistEntityException(Theme.class));

        return reservationDao.findAllByThemeIdAndDate(themeId, date).stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new NotExistEntityException(Reservation.class));

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }
}
