package nextstep.reservation_waiting;

import auth.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationWaitingService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }
}
