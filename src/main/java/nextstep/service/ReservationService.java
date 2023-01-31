package nextstep.service;

import auth.domain.persist.UserDetails;
import auth.support.AuthorizationException;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.domain.persist.Member;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Theme;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
import nextstep.repository.ThemeDao;
import nextstep.support.exception.api.*;
import nextstep.worker.ReservationApproveEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.domain.enumeration.ReservationStatus.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final String ADMIN = "ADMIN";
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        if (userDetails == null) {
            throw new AuthorizationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NoSuchScheduleException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateReservationException();
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
            throw new NoSuchThemeException();
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
            throw new NoSuchReservationException();
        }

        if (!reservation.sameMember(new Member(userDetails))) {
            throw new NotReservationOwnerException();
        }

        reservationDao.deleteById(id);
    }

    @Transactional
    public void approveById(Long id) {
        if (!isStatusEquals(reservationDao.findById(id), NOT_APPROVED)) {
            throw new IllegalApproveException();
        }

        reservationDao.updateStatusById(id, APPROVED.getStatus());
        eventPublisher.publishEvent(new ReservationApproveEvent(true));
    }

    public void cancelById(UserDetails userDetails, Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new NoSuchReservationException();
        }

        if (!isAdmin(userDetails) && !reservation.sameMember(new Member(userDetails))) {
            throw new NotReservationOwnerException();
        }

        switch (reservation.getStatus()) {
            case NOT_APPROVED:
                reservationDao.updateStatusById(id, CANCELED.getStatus());
                break;
            case APPROVED:
                reservationDao.updateStatusById(id, WAIT_CANCEL.getStatus());
                break;
            default:
                throw new IllegalCancelException();
        }
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getRole().equals(ADMIN);
    }

    public void rejectById(Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new NoSuchReservationException();
        }

        if (isStatusEquals(reservation, APPROVED)) {
            eventPublisher.publishEvent(new ReservationApproveEvent(false));
        }

        reservationDao.updateStatusById(id, REJECTED.getStatus());
    }

    private boolean isStatusEquals(Reservation reservation, ReservationStatus status) {
        return reservation.getStatus().equals(status);
    }
}
