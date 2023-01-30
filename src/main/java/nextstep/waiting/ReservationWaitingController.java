package nextstep.waiting;

import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    ResponseEntity getReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaiting> reservationWaitings = reservationWaitingProxyService.getReservationWaitings(Member.of(userDetails));

        return ResponseEntity.ok(reservationWaitings);
    }

    @DeleteMapping("/{id}")
    ResponseEntity removeReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationWaitingProxyService.deleteById(Member.of(userDetails).getId(), id);

        return ResponseEntity.noContent().build();
    }
}
