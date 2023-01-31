package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    public final ReservationService reservationService;

    public ReservationWaitingController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping()
    public ResponseEntity createWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(userDetails, reservationRequest, true);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity getMyReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaitingResponse> myReservationWaitings = reservationService.findMyReservationWaitings(userDetails);
        return ResponseEntity.ok(myReservationWaitings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.deleteWaitingById(userDetails, id);

        return ResponseEntity.noContent().build();
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
