package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.UserDetail;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember UserDetail userDetail, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationWaitingService.create(userDetail, reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember UserDetail userDetail, @PathVariable Long id) {
        reservationWaitingService.deleteById(userDetail, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> showMyReservationWaiting(@LoginMember UserDetail userDetail) {
        List<ReservationWaitingResponse> reservationWaiting = reservationWaitingService.findAllByMember(userDetail);
        return ResponseEntity.ok(reservationWaiting);
    }
}
