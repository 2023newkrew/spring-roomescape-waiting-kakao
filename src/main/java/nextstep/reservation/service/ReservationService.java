package nextstep.reservation.service;

import auth.support.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.repository.MemberDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.repository.ReservationDao;
import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.reservationwaiting.repository.ReservationWaitingDao;
import nextstep.schedule.domain.Schedule;
import nextstep.schedule.repository.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.domain.Theme;
import nextstep.theme.repository.ThemeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao, ReservationWaitingDao reservationWaitingDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        checkEmptyMember(member);

        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        checkEmptySchedule(schedule);

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        checkDuplicatedReservation(reservation);

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    private void checkEmptyMember(Member member) {
        if (member == null) {
            throw new AuthenticationException();
        }
    }

    private void checkEmptySchedule(Schedule schedule) {
        if (schedule == null) {
            throw new NullPointerException();
        }
    }


    private void checkDuplicatedReservation(List<Reservation> reservation) {
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        checkEmptyTheme(theme);

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    private void checkEmptyTheme(Theme theme) {
        if (theme == null) {
            throw new NullPointerException();
        }
    }

    public List<ReservationResponse> findAllByMemberId(Member member) {
        checkEmptyMember(member);

        return reservationDao.findAllByMemberId(member.getId())
                .stream()
                .map(ReservationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        checkEmptyReservation(reservation);
        checkReservationIsMine(member, reservation);

        reservationDao.deleteById(id);

        // get reservationWaiting which has first priority for this reservation
        ReservationWaiting reservationWaiting = reservationWaitingDao.findByScheduleId(reservation.getSchedule().getId());

        // if there is no reservationWaiting, do nothing
        if (reservationWaiting == null) {
            return;
        }

        reservationWaitingDao.deleteById(reservationWaiting.getId());

        reservationDao.save(Reservation.of(reservationWaiting));
    }

    private void checkReservationIsMine(Member member, Reservation reservation) {
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
    }

    private void checkEmptyReservation(Reservation reservation) {
        if (reservation == null) {
            throw new NullPointerException();
        }
    }
}
