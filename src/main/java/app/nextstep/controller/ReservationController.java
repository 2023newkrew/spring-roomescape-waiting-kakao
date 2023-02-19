package app.nextstep.controller;

import app.auth.support.AuthenticationException;
import app.auth.support.LoginUser;
import app.nextstep.domain.Member;
import app.nextstep.domain.Reservation;
import app.nextstep.domain.ReservationWaiting;
import app.nextstep.dto.ReservationRequest;
import app.nextstep.dto.ReservationResponse;
import app.nextstep.dto.ReservationWaitingResponse;
import app.nextstep.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginUser Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createReservation(reservationRequest.toReservation(member.getId()));
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createWaiting(@LoginUser Member member, @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.createWaiting(reservationRequest.toReservation(member.getId()));
        if (reservation.isConfirmed()) {
            return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservation-waitings/" + reservation.getId())).build();
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginUser Member member, @PathVariable Long id) {
        reservationService.deleteReservation(id, member.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteWaiting(@LoginUser Member member, @PathVariable Long id) {
        reservationService.deleteWaiting(id, member.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationResponse>> getReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> reservations = reservationService.findByThemeIdAndDate(themeId, LocalDate.parse(date));
        List<ReservationResponse> responseBody = new ArrayList<>();
        for (Reservation reservation : reservations) {
            responseBody.add(new ReservationResponse(reservation));
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/reservations/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationResponse>> getMyReservations(@LoginUser Member member) {
        List<Reservation> reservations = reservationService.findMyReservations(member.getId());
        List<ReservationResponse> responseBody = new ArrayList<>();
        for (Reservation reservation : reservations) {
            responseBody.add(new ReservationResponse(reservation));
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/reservation-waitings/mine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReservationWaitingResponse>> getMyWaitings(@LoginUser Member member) {
        List<ReservationWaiting> waitings = reservationService.findMyWaitings(member.getId());
        List<ReservationWaitingResponse> responseBody = new ArrayList<>();
        for (ReservationWaiting waiting : waitings) {
            responseBody.add(new ReservationWaitingResponse(waiting));
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
