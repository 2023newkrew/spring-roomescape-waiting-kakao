package roomescape.nextstep.reservation;

import org.springframework.http.HttpStatus;
import roomescape.auth.LoginMember;
import roomescape.auth.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.AuthenticationException;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationWaitingController {
    public final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservations-waitings")
    public ResponseEntity createReservation(@LoginMember UserDetails member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Long id = reservationWaitingService.create(member.getUsername(), reservationWaitingRequest);
        if (reservationWaitingService.getWaitingNum(reservationWaitingRequest, id) == 0)
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        return ResponseEntity.created(URI.create("/reservations-waitings/" + id)).build();
    }

    @GetMapping("/reservations-waitings/mine")
    public ResponseEntity<List<ReservationWaiting>> findMyReservationsWaitings(@LoginMember UserDetails member) {
        if (member == null) {
            throw new AuthenticationException();
        }
        return ResponseEntity.ok(
                reservationWaitingService.findAllByUsername(member.getUsername())
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
