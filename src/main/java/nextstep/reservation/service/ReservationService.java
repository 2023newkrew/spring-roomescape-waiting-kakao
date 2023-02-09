package nextstep.reservation.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nextstep.common.annotation.AdminRequired;
import nextstep.common.Lock;
import nextstep.error.ErrorCode;
import nextstep.error.exception.RoomReservationException;
import nextstep.member.Member;
import nextstep.reservation.dao.ReservationDao;
import nextstep.reservation.dao.ReservationWaitingDao;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationStatus;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.event.ReservationApproveCancelEvent;
import nextstep.reservation.event.ReservationApproveEvent;
import nextstep.reservation.event.ReservationRefuseEvent;
import nextstep.revenue.RevenueDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ReservationWaitingDao reservationWaitingDao;
    private final ApplicationEventPublisher applicationEventPublisher;
    public static final Lock reservationListLock = new Lock();

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, ScheduleDao scheduleDao,
                              ReservationWaitingDao reservationWaitingDao,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.reservationWaitingDao = reservationWaitingDao;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (Objects.isNull(member)) {
            throw new RoomReservationException(ErrorCode.AUTHENTICATION_REQUIRED);
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId()).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.SCHEDULE_NOT_FOUND);
        });
        reservationListLock.lock();
        List<Reservation> reservation = reservationDao.findValidByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            reservationListLock.unlock();
            throw new RoomReservationException(ErrorCode.DUPLICATE_RESERVATION);
        }
        Reservation newReservation = new Reservation(
                schedule,
                member
        );
        long savedId = reservationDao.save(newReservation);
        reservationListLock.unlock();
        return savedId;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.THEME_NOT_FOUND);
        });
        return reservationDao.findAllByThemeIdAndDate(theme.getId(), date);
    }

    @Transactional(readOnly = true)
    public Reservation findById(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        return reservation;
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        reservationDao.deleteById(id);
        passNextWaiting(reservation);
    }

    @Transactional(readOnly = true)
    public List<Reservation> lookUp(Member member) {
        return reservationDao.findByMemberId(member.getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_MEMBER);
        }));
    }

    @AdminRequired
    public void approveReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.approve();
        applicationEventPublisher.publishEvent(new ReservationApproveEvent(reservation));
        reservationDao.save(reservation);
    }

    public void cancelReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        checkAuthorizationOfReservation(reservation, member);
        reservation.cancel();
        reservationDao.save(reservation);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            passNextWaiting(reservation);
        }
    }

    @AdminRequired
    public void refuseReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.refuse();
        applicationEventPublisher.publishEvent(new ReservationRefuseEvent(reservation));
        reservationDao.save(reservation);
        passNextWaiting(reservation);
    }

    @AdminRequired
    public void approveCancelOfReservation(Member member, Long id) {
        Reservation reservation = getReservation(id);
        reservation.approveCancel();
        applicationEventPublisher.publishEvent(new ReservationApproveCancelEvent(reservation));
        reservationDao.save(reservation);
        passNextWaiting(reservation);
    }

    private Reservation getReservation(Long id) {
        return reservationDao.findById(id).orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        });
    }

    private void checkAuthorizationOfReservation(Reservation reservation, Member member) {
        if (!reservation.isMine(member)) {
            throw new RoomReservationException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    private void passNextWaiting(Reservation reservation) {
        Optional<ReservationWaiting> reservationWaiting = reservationWaitingDao
                .findFirstByScheduleId(
                        reservation.getSchedule().getId()
                );
        if (reservationWaiting.isEmpty()) {
            return;
        }
        reservationWaitingDao.deleteById(reservationWaiting.get().getId().orElseThrow(() -> {
            throw new RoomReservationException(ErrorCode.INVALID_RESERVATION_WAITING);
        }));
        reservationDao.save(reservationWaiting.get().convertToReservation());
    }
}
