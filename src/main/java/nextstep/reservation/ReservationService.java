package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetail;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.DuplicatedReservationException;
import nextstep.member.MemberDao;
import nextstep.member.MemberService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
    public final ReservationDao reservationDao;
    private final MemberService memberService;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(UserDetail userDetail, ReservationRequest reservationRequest) {
        if (userDetail == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicatedReservationException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                memberService.toMember(userDetail)
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

    public void deleteById(UserDetail userDetail, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (userDetail == null || !reservation.getMember().getId().equals(userDetail.getId())) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMember(UserDetail userDetail) {
        List<Reservation> reservations = reservationDao.findAllByMemberId(userDetail.getId());
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
