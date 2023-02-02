package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
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
    public ResponseEntity<Void> createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<ReservationResponse>> showMyReservations(@LoginMember Member member) {
        List<ReservationResponse> reservations = reservationService.findAllByMember(member);
        return ResponseEntity.ok(reservations);
    }


    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(@LoginMember Member member, @PathVariable Long id) {
        ReservationResponse reservationResponse = reservationService.cancelReservation(member, id);
        return ResponseEntity.ok(reservationResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
