package roomwaiting.nextstep.reservation.service;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.Schedule;
import roomwaiting.nextstep.schedule.ScheduleDao;
import roomwaiting.nextstep.member.MemberDao;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.dao.ReservationDao;
import roomwaiting.nextstep.theme.ThemeDao;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

import static roomwaiting.support.Messages.*;

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
            throw new AuthenticationServiceException(LOGIN_NEEDS.getMessage());
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()).orElseThrow(() ->
                new NullPointerException(SCHEDULE_NOT_FOUND.getMessage() + ID + reservationRequest.getScheduleId()));
        reservationDao.findByScheduleId(schedule.getId()).ifPresent(val -> {
            throw new DuplicateKeyException(ALREADY_REGISTERED_RESERVATION.getMessage());
        });
        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        themeDao.findById(themeId).orElseThrow(() ->
                new NullPointerException(THEME_NOT_FOUND.getMessage() + ID + themeId));
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id).orElseThrow(() ->
                new NullPointerException(RESERVATION_NOT_FOUND.getMessage() + ID + id));
        if (!reservation.sameMember(member)) {
            throw new AuthorizationServiceException(NOT_PERMISSION_DELETE.getMessage());
        }
        reservationDao.deleteById(id);
    }

    public List<Reservation> lookUp(Member member) {
        return reservationDao.findAllByMemberId(member.getId());
    }
}
