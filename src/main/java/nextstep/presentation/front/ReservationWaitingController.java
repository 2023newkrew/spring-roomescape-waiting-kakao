package nextstep.presentation.front;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/reservation-waitings")
@RestController
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @DeleteMapping("/{reservationWaitingId}")
    public ResponseEntity<Void> deleteReservationWaiting(@AuthenticationPricipal LoginMember loginMember, @PathVariable Long reservationWaitingId){
        reservationWaitingService.deleteReservationWaitingById(loginMember.getId(), reservationWaitingId);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> findMyReservationsWaitings(@AuthenticationPricipal LoginMember loginMember) {
        List<ReservationWaitingResponse> reservationWaitings = reservationWaitingService.findMyReservationWaitings(loginMember.getId());

        return ResponseEntity.ok(reservationWaitings);
    }

}
