package app.nextstep.controller;

import app.auth.support.AuthenticationException;
import app.auth.support.LoginUser;
import app.nextstep.domain.Member;
import app.nextstep.domain.Reservation;
import app.nextstep.dto.ReservationRequest;
import app.nextstep.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
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

    @GetMapping("/reservations")
    public ResponseEntity getReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, LocalDate.parse(date));
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginUser Member member, @PathVariable Long id) {
        reservationService.delete(id, member.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createWaiting(@LoginUser Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createWaiting(reservationRequest.toReservation(member.getId()));
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
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
