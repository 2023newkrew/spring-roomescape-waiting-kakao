package nextstep.waiting;

import auth.LoginMember;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody WaitingRequest waitingRequest) {
        CreateWaitingResponse response = waitingService.create(member, waitingRequest);
        return ResponseEntity.created(URI.create(
                response.isReservedDirectly()
                        ? "/reservations/" + response.getReservation().getId()
                        : "/reservation-waitings/" + response.getWaiting().getId()))
                .build();
    }
}
