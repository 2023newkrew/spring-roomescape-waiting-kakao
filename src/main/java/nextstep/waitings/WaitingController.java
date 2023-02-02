package nextstep.waitings;

import auth.AuthorizationException;
import auth.LoginMember;
import auth.userauth.UserAuth;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember UserAuth userAuth, @RequestBody ReservationRequest reservationRequest) {
        URI Uri = URI.create(waitingService.create(new Member(userAuth), reservationRequest));
        return ResponseEntity.created(Uri).build();
    }

    @DeleteMapping("/{waitingId}")
    public ResponseEntity<Void> delete(@LoginMember UserAuth userAuth, @PathVariable Long waitingId){
        try {
            waitingService.delete(new Member(userAuth), waitingId);
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> readMyWaitings(@LoginMember UserAuth userAuth) {
        List<WaitingResponse> waitingResponses = waitingService.findAllByMemberId(new Member(userAuth));

        return ResponseEntity.ok().body(waitingResponses);
    }
}
