package nextstep.reservation;

import auth.AuthorizationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.DuplicatedEntityException;
import nextstep.exceptions.exception.NotExistEntityException;
import nextstep.exceptions.exception.NotLoggedInException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new NotLoggedInException();
        }

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicatedEntityException();
        }

        Reservation newReservation = Reservation.builder()
                .schedule(schedule)
                .member(member)
                .build();

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId).orElseThrow(NotExistEntityException::new);
        return changeToResponse(reservationDao.findAllByThemeIdAndDate(themeId, date));
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(NotExistEntityException::new);
        if (!reservation.isCreatedBy(member)) {
            throw new AuthorizationException();
        }
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAllByMemberId(Long memberId) {
        return changeToResponse(reservationDao.findByMemberId(memberId));
    }

    private List<ReservationResponse> changeToResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::of)
                .collect(Collectors.toList());
    }
}
