package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.userauth.UserAuth;
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
    public ResponseEntity<Void> createReservation(@LoginMember UserAuth userAuth, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(new Member(userAuth), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember UserAuth userAuth, @PathVariable Long id) {
        reservationService.deleteById(new Member(userAuth), id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<ReservationResponse>> readMyReservations(@LoginMember UserAuth userAuth) {
        List<ReservationResponse> reservationResponses = reservationService.findAllByMemberId(new Member(userAuth));

        return ResponseEntity.ok().body(reservationResponses);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
