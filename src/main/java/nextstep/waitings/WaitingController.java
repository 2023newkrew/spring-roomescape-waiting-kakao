package nextstep.waitings;

import auth.AuthorizationException;
import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.support.NotExistEntityException;
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
    public ResponseEntity<Void> create(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        URI Uri = URI.create(waitingService.create(new Member(userDetails), reservationRequest));
        return ResponseEntity.created(Uri).build();
    }

    @DeleteMapping("/{waitingId}")
    public ResponseEntity<Void> delete(@LoginMember UserDetails userDetails, @PathVariable Long waitingId){
        try {
            waitingService.delete(new Member(userDetails), waitingId);
        } catch (NotExistEntityException e) {
            return ResponseEntity.badRequest().build();
        } catch (AuthorizationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> readMyWaitings(@LoginMember UserDetails userDetails) {
        List<WaitingResponse> waitingResponses = waitingService.findAllByMemberId(new Member(userDetails));

        return ResponseEntity.ok().body(waitingResponses);
    }
}
