package nextstep.service;

import auth.domain.persist.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import nextstep.support.exception.api.reservation.*;
import nextstep.support.exception.api.schedule.NoSuchScheduleException;
import nextstep.support.exception.api.theme.NoSuchThemeException;
import nextstep.worker.ReservationApproveEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.domain.enumeration.ReservationStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private static final String ADMIN = "ADMIN";
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ScheduleDao scheduleDao;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long create(UserDetails userDetails, ReservationRequest reservationRequest) {
        Schedule schedule = findScheduleById(reservationRequest);
        validateDuplicationReservation(schedule);
        return reservationDao.save(new Reservation(schedule, new Member(userDetails)));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        findThemeById(themeId);
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllByUserId(Long id) {
        return reservationDao.findAllByUserId(id).stream().map(ReservationResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(UserDetails userDetails, Long id) {
        Reservation reservation = findReservationById(id);

        if (!reservationOwner(userDetails, reservation)) {
            throw new NotReservationOwnerException();
        }

        reservationDao.deleteById(id);
    }

    @Transactional
    public void approveById(Long id) {
        Reservation reservation = findReservationById(id);

        if (!statusEquals(reservation, NOT_APPROVED)) {
            throw new IllegalReservationApproveException();
        }

        reservationDao.updateStatusById(id, APPROVED.getStatus());

        eventPublisher.publishEvent(new ReservationApproveEvent(true));
    }

    @Transactional
    public void cancelById(UserDetails userDetails, Long id) {
        Reservation reservation = findReservationById(id);

        if (!isAdmin(userDetails) && !reservationOwner(userDetails, reservation)) {
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
    public void rejectById(Long id) {
        Reservation reservation = findReservationById(id);

        if (statusEquals(reservation, APPROVED)) {
            eventPublisher.publishEvent(new ReservationApproveEvent(false));
        }

        reservationDao.updateStatusById(id, REJECTED.getStatus());
    }

    @Transactional
    public void approveCancelById(Long id) {
        Reservation reservation = findReservationById(id);

        if (!statusEquals(reservation, WAIT_CANCEL)) {
            throw new IllegalReservationCancelException();
        }

        reservationDao.updateStatusById(id, CANCELED.getStatus());

        eventPublisher.publishEvent(new ReservationApproveEvent(false));
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getRole().equals(ADMIN);
    }

    @Transactional
    public Reservation findReservationById(Long id) {
        Reservation reservation = reservationDao.findById(id);

        if (reservation == null) {
            throw new NoSuchReservationException();
        }
        return reservation;
    }

    private Schedule findScheduleById(ReservationRequest reservationRequest) {
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NoSuchScheduleException();
        }
        return schedule;
    }


    private void findThemeById(Long themeId) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NoSuchThemeException();
        }
    }

    private boolean statusEquals(Reservation reservation, ReservationStatus status) {
        return reservation.getStatus().equals(status);
    }

    private void validateDuplicationReservation(Schedule schedule) {
        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateReservationException();
        }
    }

    private boolean reservationOwner(UserDetails userDetails, Reservation reservation) {
        return reservation.sameMember(new Member(userDetails));
    }
}
