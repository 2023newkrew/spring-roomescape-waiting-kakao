package app.nextstep.service;

import app.auth.support.AuthenticationException;
import app.nextstep.domain.Reservation;
import app.nextstep.domain.ReservationWaiting;
import app.nextstep.repository.ReservationRepository;
import app.nextstep.support.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    public final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date) {
        return reservationRepository.findByThemeIdAndDate(themeId, date);
    }

    public List<Reservation> findMyReservations(Long memberId) {
        return reservationRepository.findConfirmedByMemberId(memberId);
    }

    public List<ReservationWaiting> findMyWaitings(Long memberId) {
        return reservationRepository.findWaitingByMemberId(memberId);
    }

    public Long createReservation(Reservation reservation) {
        List<Reservation> reservations = reservationRepository.findByScheduleId(reservation.getSchedule().getId());
        if (!reservations.isEmpty()) {
            throw new RuntimeException();
        }
        reservation.confirmed();
        return reservationRepository.save(reservation);
    }

    public Reservation createWaiting(Reservation reservation) {
        reservation.waiting();
        List<Reservation> reservations = reservationRepository.findByScheduleId(reservation.getSchedule().getId());
        if (reservations.isEmpty()) {
            reservation.confirmed();
        }
        return new Reservation(reservationRepository.save(reservation), reservation.getStatus());
    }

    public void deleteReservation(Long id, Long memberId) {
        Reservation reservation = reservationRepository.findById(id);

        if (reservation == null || reservation.getStatus() != "CONFIRMED") {
            throw new EntityNotFoundException();
        }
        if (!reservation.isReservationOf(memberId)) {
            throw new AuthenticationException();
        }
        reservationRepository.delete(id);
    }

    public void deleteWaiting(Long id, Long memberId) {
        Reservation reservation = reservationRepository.findById(id);

        if (reservation == null || reservation.getStatus() != "WAITING") {
            throw new EntityNotFoundException();
        }
        if (!reservation.isReservationOf(memberId)) {
            throw new AuthenticationException();
        }
        reservationRepository.delete(id);
    }
}
