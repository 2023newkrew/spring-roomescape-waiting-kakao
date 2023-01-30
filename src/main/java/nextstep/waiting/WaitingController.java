package nextstep.waiting;

import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createWaiting(@LoginMember Member member, @RequestBody WaitingRequest waitingRequest) {
        CreateWaitingResponse response = waitingService.create(member, waitingRequest);
        return ResponseEntity.created(URI.create(
                response.isReservedDirectly()
                        ? "/reservations/" + response.getReservation().getId()
                        : "/reservation-waitings/" + response.getWaiting().getId()))
                .build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<MyWaitingResponse>> myWaitings(@LoginMember Member member) {
        List<MyWaitingResponse> response = waitingService.findAllWaitingsByMemberId(member.getId());
        return ResponseEntity.ok().body(response);
    }
}
