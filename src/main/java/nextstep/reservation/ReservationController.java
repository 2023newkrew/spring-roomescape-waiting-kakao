package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(new Member(userDetails), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.deleteById(new Member(userDetails), id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> readMyReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> reservationResponses = reservationService.findAllByMemberId(new Member(userDetails));

        return ResponseEntity.ok().body(reservationResponses);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable Long id) {
        reservationService.approveReservation(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        if (userDetails.getRole().equals("ADMIN")) {
            reservationService.rejectReservation(id);
        }
        else {
            reservationService.cancelReservation(id);
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel-approve")
    public ResponseEntity<Void> cancelApproveReservation(@PathVariable Long id) {
        reservationService.cancelApproveReservation(id);
        return ResponseEntity.ok().build();
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
