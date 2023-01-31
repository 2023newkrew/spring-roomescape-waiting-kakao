package roomescape.nextstep.reservation;

import roomescape.auth.AuthenticationException;
import roomescape.auth.LoginMember;
import roomescape.auth.UserDetails;
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

    @PostMapping({"/reservations", "/reservations-waitings"})
    public ResponseEntity createReservation(@LoginMember UserDetails member, @RequestBody ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Reservation reservation = reservationService.create(member.getUsername(), reservationRequest);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations-waitings/" + reservation.getId())).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findFilteredReservationsByThemeIdAndDate(themeId, date, ReservationStatus.CONFIRMED);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails member, @PathVariable Long id) {
        if (member == null) {
            throw new AuthenticationException();
        }
        reservationService.deleteById(member.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-waitings/mine")
    public ResponseEntity<List<ReservationWaiting>> findMyReservationsWaitings(@LoginMember UserDetails member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        return ResponseEntity.ok(
                reservationService.findAllByUsername(member.getUsername())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
