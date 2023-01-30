package nextstep.reservation;

import auth.AuthenticationException;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

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

    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(userDetails.getId());

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
                member,
                0L
        );

        return reservationDao.save(newReservation);
    }

    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date)
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationResponse> findMyReservations(UserDetails userDetails) {
        return reservationDao.findByMemberId(userDetails.getId())
                .stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationWaitingResponse> findMyReservationWaitings(UserDetails userDetails) {
        return reservationDao.findWaitingByMemberId(userDetails.getId())
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);

        reservationDao.adjustWaitingNumByScheduleIdAndBaseNum(reservation.getSchedule().getId(), 0L);
    }

    public Long createWaiting(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        Member member = memberDao.findById(userDetails.getId());

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());

        Reservation newReservation = new Reservation(
                schedule,
                member,
                (long) reservation.size()
        );

        return reservationDao.save(newReservation);
    }

    public void deleteWaitingById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findWaitingById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(userDetails)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteWaitingById(id);

        reservationDao.adjustWaitingNumByScheduleIdAndBaseNum(reservation.getSchedule().getId(), reservation.getWaitNum());
    }
}
