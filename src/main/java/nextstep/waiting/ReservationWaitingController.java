package nextstep.waiting;

import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingProxyService reservationWaitingProxyService;

    public ReservationWaitingController(ReservationWaitingProxyService reservationWaitingProxyService) {
        this.reservationWaitingProxyService = reservationWaitingProxyService;
    }

    @PostMapping
    ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest request) {
        ReservationWaitingResponse response = reservationWaitingProxyService.makeReservation(Member.of(userDetails), request.getScheduleId());
        if (response.getWaiting()) {
            return ResponseEntity.created(URI.create("/reservations/" + response.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservation_waitings/" + response.getId())).build();
    }


}
