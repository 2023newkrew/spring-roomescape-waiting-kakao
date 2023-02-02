package nextstep.waiting;

import auth.LoginMember;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationService reservationService;
    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationService reservationService, ReservationWaitingService reservationWaitingService) {
        this.reservationService = reservationService;
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<?> createReservationWaiting(@LoginMember Long memberId, @RequestBody ReservationWaitingRequest request) {
        if (!reservationService.existsByScheduleId(request.getScheduleId())){
            Long reservationId = reservationService.create(memberId, new ReservationRequest(request.getScheduleId()));
            return ResponseEntity.created(URI.create("/reservations/" + reservationId)).build();
        }


        Long reservationWaitingId = reservationWaitingService.save(memberId, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + reservationWaitingId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember Long memberId, @PathVariable Long id) {
        reservationWaitingService.deleteById(memberId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity findMyReservationWaitings(@LoginMember Long memberId) {
        List<ReservationWaitingResponse> reservationWaitings
                = reservationWaitingService.findByMemberId(memberId).stream()
                .map(ReservationWaitingResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(reservationWaitings);
    }
}
