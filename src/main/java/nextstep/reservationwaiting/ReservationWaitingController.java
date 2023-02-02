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
    public ResponseEntity<Void> createReservationWaiting(@LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.create(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> findMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.findMyReservationWaitings(member);
        return ResponseEntity.ok().body(results);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> cancelReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.cancelById(member, id);

        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> runtimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
