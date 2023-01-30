package nextstep.reservation.service;

import auth.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import nextstep.exception.DuplicateEntityException;
import nextstep.exception.NotExistEntityException;
import nextstep.member.dao.MemberDao;
import nextstep.member.model.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.model.CreateReservationRequest;
import nextstep.reservation.model.Reservation;
import nextstep.schedule.dao.ScheduleDao;
import nextstep.schedule.model.Schedule;
import nextstep.theme.dao.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;

    public Long create(CreateReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId())
                .orElseThrow(NotExistEntityException::new);

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());

        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(
                schedule,
                reservationRequest.getMemberName()
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date, Long loginMemberId) {
        themeDao.findById(themeId)
                .orElseThrow(NotExistEntityException::new);

        Member loginMember = memberDao.findById(loginMemberId)
                .orElseThrow(AuthenticationException::new);

        return reservationDao.findAllByThemeIdAndDateAndMemberName(themeId, date, loginMember.getMemberName());
    }

    public void deleteById(Long id, Long loginMemberId) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(NotExistEntityException::new);

        Member loginMember = memberDao.findById(loginMemberId)
                .orElseThrow(AuthenticationException::new);

        if (!reservation.isReservedBy(loginMember)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }
}
