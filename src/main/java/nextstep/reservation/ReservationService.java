package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.exception.AlreadyReservedScheduleException;
import nextstep.support.exception.AuthenticationException;
import nextstep.support.exception.DuplicateEntityException;
import nextstep.support.exception.NoReservationException;
import nextstep.theme.Theme;
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
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        reservationDao.findByScheduleId(schedule.getId())
                .orElseThrow(DuplicateEntityException::new);

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
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(NoReservationException::new);

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public void validateByMember(Reservation reservation, Member member) {
        if (reservation.sameMember(member)) {
            throw new AlreadyReservedScheduleException("이미 예약된 스케줄에 예약 대기를 생성할 수 없습니다.");
        }
    }
}
