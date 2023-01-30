package nextstep.reservationwaiting;

import auth.AuthenticationException;
import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    public final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity createReservationWaiting(@LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity findMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.findMyReservationWaitings(member);
        return ResponseEntity.ok().body(results);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
