package app.nextstep.controller;

import app.auth.support.AuthenticationException;
import app.auth.support.AuthenticationPrincipal;
import app.auth.service.UserDetail;
import app.nextstep.domain.Reservation;
import app.nextstep.dto.ReservationRequest;
import app.nextstep.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@AuthenticationPrincipal UserDetail userDetail, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(userDetail.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@AuthenticationPrincipal UserDetail userDetail, @PathVariable Long id) {
        reservationService.deleteById(userDetail.getId(), id);

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
