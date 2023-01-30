package nextstep.service;

import auth.domain.persist.UserDetails;
import auth.support.AuthenticationException;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.persist.Member;
import nextstep.repository.MemberDao;
import nextstep.domain.persist.Reservation;
import nextstep.repository.ReservationDao;
import nextstep.domain.persist.Schedule;
import nextstep.repository.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.domain.persist.Theme;
import nextstep.repository.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
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
                new Member(userDetails)
        );

        return reservationDao.save(newReservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByUserId(Long id) {
        return reservationDao.findAllByUserId(id).stream().map(ReservationResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(new Member(userDetails))) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }
}
