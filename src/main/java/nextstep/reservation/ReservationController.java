package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.support.UnsupportedReservationStatusException;
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
    public ResponseEntity createReservation(@LoginMember UserDetails member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reservations/{id}/approve")
    public ResponseEntity approveReservation(@PathVariable Long id) {
        reservationService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity cancelReservation(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationService.cancel(member, id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reservations/{id}/reject")
    public ResponseEntity rejectReservation(@PathVariable Long id) {
        reservationService.reject(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/reservations/{id}/cancel-approve")
    public ResponseEntity cancelApproveReservation(@PathVariable Long id) {
        reservationService.cancelApprove(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(UnsupportedReservationStatusException.class)
    public ResponseEntity onUnsupportedReservationStatusException(UnsupportedReservationStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
