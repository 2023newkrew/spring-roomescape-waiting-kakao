package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.exception.*;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(Member member, ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NonExistScheduleException::new);

        Optional<Reservation> byScheduleId = reservationDao.findByScheduleId(schedule.getId());
        byScheduleId
                .ifPresent(reservation -> {
                    throw new DuplicateReservationException();
                });

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public Optional<Reservation> findByScheduleId(Long scheduleId) {
        return reservationDao.findByScheduleId(scheduleId);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        if (themeDao.findById(themeId)
                .isEmpty()) {
            throw new NonExistThemeException();
        }
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Reservation reservation, Member member) {
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(reservation.getId());
    }

    public void validateByMember(Reservation reservation, Member member) {
        if (reservation.sameMember(member)) {
            throw new AlreadyReservedScheduleException();
        }
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id)
                .orElseThrow(NonExistReservationException::new);
    }

    public List<Reservation> findOwn(Member member) {
        return reservationDao.findAllByMemberId(member.getId());
    }
}
