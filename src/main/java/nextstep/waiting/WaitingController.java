package nextstep.waiting;

import auth.LoginMember;
import auth.UserDetails;
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
    public ResponseEntity<Void> waitReservation(@LoginMember UserDetails userDetails,
                                                @RequestBody WaitingRequest waitingRequest) {
        WaitingRegisterStatus waitingRegisterStatus = waitingService.waitReservation(userDetails, waitingRequest);
        if (waitingRegisterStatus.isRegisteredAsWaiting()) {
            return ResponseEntity.created(URI.create("/reservation-waitings/" + waitingRegisterStatus.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations/" + waitingRegisterStatus.getId())).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> getWaiting(@LoginMember UserDetails userDetails) {
        List<WaitingResponse> waitingResponses = waitingService.getWaiting(userDetails);
        return ResponseEntity.ok(waitingResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteWaitingReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        waitingService.deleteById(userDetails, id);
        return ResponseEntity.noContent().build();
    }
}
