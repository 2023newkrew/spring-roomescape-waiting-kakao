package nextstep.reservation.controller;

import auth.model.MemberDetails;
import auth.support.AuthenticationPrincipal;
import auth.support.LoginRequired;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.model.CreateReservationWaiting;
import nextstep.reservation.model.ReservationWaitingRequest;
import nextstep.reservation.model.ReservationWaitingResponse;
import nextstep.reservation.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
@RequiredArgsConstructor
public class ReservationWaitingController {
    public final ReservationWaitingService reservationWaitingService;

    @LoginRequired
    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> getMyReservationWaiting(@AuthenticationPrincipal MemberDetails memberDetails){
        Long memberId = memberDetails.getId();
        List<ReservationWaitingResponse> reservationWaitings = reservationWaitingService.findByMemberId(memberId, memberId);
        return ResponseEntity.ok().body(reservationWaitings);
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> applyReservation(@AuthenticationPrincipal MemberDetails memberDetails
            , @RequestBody ReservationWaitingRequest reservationWaitingRequest){
        CreateReservationWaiting createReservationWaiting = CreateReservationWaiting.from(reservationWaitingRequest
                , memberDetails.getId()
                , memberDetails.getMemberName());

        Long id = reservationWaitingService.create(createReservationWaiting);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @LoginRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservationWaiting(@AuthenticationPrincipal MemberDetails memberDetails
            , @PathVariable(required = true) Long id){
        reservationWaitingService.deleteById(memberDetails.getId(), id);
        return ResponseEntity.noContent().build();
    }
}