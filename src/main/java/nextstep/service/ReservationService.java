package nextstep.service;

import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.enumeration.ReservationStatus;
import nextstep.domain.persist.Reservation;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Waiting;
import nextstep.repository.ReservationDao;
import nextstep.repository.ScheduleDao;
import nextstep.repository.WaitingDao;
import nextstep.support.exception.api.reservation.IllegalReservationApproveException;
import nextstep.support.exception.api.reservation.IllegalReservationCancelException;
import nextstep.support.exception.api.reservation.NoSuchReservationException;
import nextstep.support.exception.api.reservation.NotReservationOwnerException;
import nextstep.support.exception.api.schedule.NoSuchScheduleException;
import nextstep.worker.ReservationApproveEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.domain.enumeration.ReservationStatus.*;
import static nextstep.support.constant.ProfitSettings.*;
import static nextstep.support.converter.UserDetailToMemberConverter.convertUserDetailToMember;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private static final String ADMIN = "ADMIN";
    private static final String RESERVATION_WAITING = "reservation-waitings";
    private static final String RESERVATION = "resrvations";

    private final ReservationDao reservationDao;
    private final ScheduleDao scheduleDao;
    private final WaitingDao waitingDao;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public String addReservation(UserDetails userDetails, ReservationRequest reservationRequest) {
        Schedule schedule = getSchedule(reservationRequest);

        if (isReservationAlreadyExist(schedule)) {
            return RESERVATION_WAITING + "/" + waitingDao.save(new Waiting(schedule, convertUserDetailToMember(userDetails)));
        }
        return RESERVATION + "/" + reservationDao.save(new Reservation(schedule, convertUserDetailToMember(userDetails)));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByThemeIdAndDate(Long themeId, String date) {
        return reservationDao.findAllByThemeIdAndDate(themeId, date).stream().map(ReservationResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByUserId(Long userId) {
        return reservationDao.findAllByUserId(userId).stream().map(ReservationResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void removeReservation(UserDetails userDetails, Long id) {
        Reservation reservation = getReservation(id);

        if (isNotReservationOwner(userDetails, reservation)) {
            throw new NotReservationOwnerException();
        }

        reservationDao.deleteById(id);
    }

    @Transactional
    public void approveReservation(Long id) {
        Reservation reservation = getReservation(id);

        if (!statusEquals(reservation, NOT_APPROVED)) {
            throw new IllegalReservationApproveException();
        }

        reservationDao.updateStatusById(id, APPROVED.getStatus());

        eventPublisher.publishEvent(new ReservationApproveEvent(Reserved));
    }

    @Transactional
    public void cancelReservation(UserDetails userDetails, Long id) {
        Reservation reservation = getReservation(id);

        if (isNotAdmin(userDetails) && isNotReservationOwner(userDetails, reservation)) {
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
                throw new IllegalReservationCancelException();
        }
    }

    @Transactional
    public void rejectReservation(Long id) {
        Reservation reservation = getReservation(id);

        if (statusEquals(reservation, APPROVED)) {
            eventPublisher.publishEvent(new ReservationApproveEvent(Rejected));
        }

        reservationDao.updateStatusById(id, REJECTED.getStatus());
    }

    @Transactional
    public void approveCancelReservation(Long id) {
        Reservation reservation = getReservation(id);

        if (!statusEquals(reservation, WAIT_CANCEL)) {
            throw new IllegalReservationCancelException();
        }

        reservationDao.updateStatusById(id, CANCELED.getStatus());

        eventPublisher.publishEvent(new ReservationApproveEvent(Canceled));
    }

    private boolean isNotAdmin(UserDetails userDetails) {
        return !userDetails.getRole().equals(ADMIN);
    }

    @Transactional
    public Reservation getReservation(Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new NoSuchReservationException();
        }
        return reservation;
    }

    private Schedule getSchedule(ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NoSuchScheduleException();
        }
        return schedule;
    }

    private boolean statusEquals(Reservation reservation, ReservationStatus status) {
        return reservation.getStatus().equals(status);
    }

    private Boolean isReservationAlreadyExist(Schedule schedule) {
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());

        return !reservation.isEmpty();
    }

    private boolean isNotReservationOwner(UserDetails userDetails, Reservation reservation) {
        return !reservation.sameMember(convertUserDetailToMember(userDetails));
    }
}
