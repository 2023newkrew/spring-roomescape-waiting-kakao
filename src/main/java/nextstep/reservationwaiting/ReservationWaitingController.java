package nextstep.reservationwaiting;

import auth.LoginMember;
import nextstep.member.Member;
import nextstep.support.DataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    public final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<Void> reserveReservationWaiting(@LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Long id = reservationWaitingService.reserve(member, reservationWaitingRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<DataResponse<List<ReservationWaitingResponse>>> findMyReservationWaitings(@LoginMember Member member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.findMyReservationWaitings(member);
        return ResponseEntity.ok().body(DataResponse.of(results));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> cancelReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.cancelById(member, id);

        return ResponseEntity.noContent().build();
    }
}
