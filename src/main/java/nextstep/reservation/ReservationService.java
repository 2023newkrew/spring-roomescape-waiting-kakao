package nextstep.reservation;

import auth.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DoesNotExistEntityException;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.ThemeDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final MemberDao memberDao;
    private final ApplicationEventPublisher publisher;

    public Long create(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(AuthenticationException::new);
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(DoesNotExistEntityException::new);

        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId)
                .orElseThrow(DoesNotExistEntityException::new);

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Long memberId, Long id) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(AuthenticationException::new);
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(DoesNotExistEntityException::new);
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);

        publisher.publishEvent(new ReservationDeleteEvent(reservation));
    }

    public List<ReservationResponse> findByMemberId(Long memberId) {
        return reservationDao.findByMemberId(memberId)
                .stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }
}
