package app.nextstep.controller;

import app.auth.support.AuthenticationException;
import app.auth.support.LoginUser;
import app.nextstep.domain.Member;
import app.nextstep.domain.Reservation;
import app.nextstep.dto.ReservationRequest;
import app.nextstep.dto.ReservationResponse;
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

    @PostMapping("/reservation-waitings")
    public ResponseEntity create(@LoginUser Member member, @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.create(reservationRequest.toReservation(member.getId()));
        if (reservation.isConfirmed()) {
            return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservation-waitings/" + reservation.getId())).build();
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

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginUser Member member, @PathVariable Long id) {
        reservationService.delete(id, member.getId());
        return ResponseEntity.noContent().build();
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
