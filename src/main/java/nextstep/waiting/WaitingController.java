package nextstep.waiting;

import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class WaitingController {
    private static final String RESERVATION_LOCATION_PREFIX = "/reservations/";
    public static final String WAITING_LOCATION_PREFIX = "/reservation-waitings/";
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createWaiting(@LoginMember Member member, @RequestBody CreateWaitingRequest createWaitingRequest) {
        CreateWaitingResponse response = waitingService.create(member, createWaitingRequest);
        return ResponseEntity.created(URI.create(
                        (response.isReservedDirectly() ? RESERVATION_LOCATION_PREFIX : WAITING_LOCATION_PREFIX) + response.getId()))
                .build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<MyWaitingResponse>> myWaitings(@LoginMember Member member) {
        List<MyWaitingResponse> response = waitingService.findAllWaitingsByMemberId(member.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@LoginMember Member member, @PathVariable Long id) {
        waitingService.deleteById(member, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
