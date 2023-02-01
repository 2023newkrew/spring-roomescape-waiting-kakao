package nextstep.waitingreservation;

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
public class WaitingReservationController {

    public final WaitingReservationService waitingReservationService;

    public WaitingReservationController(WaitingReservationService waitingReservationService) {
        this.waitingReservationService = waitingReservationService;
    }

    @PostMapping()
    public ResponseEntity createWaiting(@LoginMember UserDetails userDetails, @RequestBody WaitingReservationRequest waitingReservationRequest) {
        Long id = waitingReservationService.create(userDetails, waitingReservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity getMyReservationWaitings(@LoginMember UserDetails userDetails) {
        List<WaitingReservationResponse> myWaitingReservations = waitingReservationService.findMyWaitingReservations(userDetails);
        return ResponseEntity.ok(myWaitingReservations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        waitingReservationService.deleteById(userDetails, id);
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
