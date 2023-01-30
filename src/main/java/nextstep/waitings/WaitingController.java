package nextstep.waitings;

import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}
