package nextstep.reservation.service;

import java.util.List;
import java.util.Objects;
import nextstep.common.annotation.AdminRequired;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.revenue.RevenueDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final RevenueDao revenueDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao,
                              ReservationWaitingDao reservationWaitingDao, RevenueDao revenueDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.revenueDao = revenueDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new RoomReservationException(ErrorCode.AUTHENTICATION_REQUIRED);
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        List<Reservation> reservation = reservationDao.findAllByScheduleId(schedule.getId());
        reservation.forEach(reservation1 -> System.out.println("DFASDFASDFA : " + reservation1.getStatus()));
        if (!reservation.isEmpty()) {
            throw new RoomReservationException(ErrorCode.DUPLICATE_RESERVATION);
        }

        Reservation newReservation = new Reservation(
                schedule,
                member
        );

        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new RoomReservationException(ErrorCode.THEME_NOT_FOUND);
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public Reservation findById(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        return reservation;
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        reservationDao.deleteById(id);
        ReservationWaiting reservationWaiting = reservationWaitingDao
                .findFirstByScheduleId(
                        reservation.getSchedule().getId()
                );
        if (Objects.isNull(reservationWaiting)) {
            return;
        }
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        reservationDao.save(reservationWaiting.convertToReservation());
    }

    public List<Reservation> lookUp(Member member) {
        return reservationDao.findByMemberId(member.getId());
    }

    @AdminRequired
    public void approveReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.approve();
        revenueDao.save(reservation.getRevenue());
        reservationDao.save(reservation);
    }

    public void cancelReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        reservation.cancel();
        reservationDao.save(reservation);
    }

    @AdminRequired
    public void refuseReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.refuse();
        if (Objects.nonNull(reservation.getRevenue())) {
            revenueDao.save(reservation.getRevenue());
        }
        reservationDao.save(reservation);
    }

    @AdminRequired
    public void approveCancelOfReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.approveCancel();
        revenueDao.save(reservation.getRevenue());
        reservationDao.save(reservation);
    }

    private Reservation getReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);
        if(Objects.isNull(reservation)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return reservation;
    }

    private void checkAuthorizationOfReservation(Reservation reservation, Member member) {
        if (!reservation.isMine(member)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }
}
