package nextstep.presentation;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/reservation-waitings")
@RestController
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationWaitingService.createReservationWaiting(loginMember.getId(), reservationRequest);

        return ResponseEntity.created(URI.create("/reservation-waitings/" + id))
                .build();
    }

    @DeleteMapping("/{reservationWaitingId}")
    public ResponseEntity<Void> deleteReservationWaiting(@AuthenticationPricipal LoginMember loginMember, @PathVariable Long reservationWaitingId){
        reservationWaitingService.deleteReservationWaitingById(loginMember.getId(), reservationWaitingId);

        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/mine")
    public ResponseEntity findMyReservationsWaitings(@AuthenticationPricipal LoginMember loginMember) {
        List<ReservationWaitingResponse> reservationWaitings = reservationWaitingService.findMyReservationWaitings(loginMember.getId());

        return ResponseEntity.ok(reservationWaitings);
    }

}
