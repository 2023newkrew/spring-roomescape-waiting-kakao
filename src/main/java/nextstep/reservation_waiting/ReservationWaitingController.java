package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.login.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation-waitings")
@RequiredArgsConstructor
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        Member member = memberService.findById(userDetails.getId());
        Optional<Reservation> findOptionalReservation = reservationService.findByScheduleId(reservationWaitingRequest.getScheduleId());
        if (findOptionalReservation.isEmpty()) {
            Long reservationId = reservationService.create(member, new ReservationRequest(reservationWaitingRequest.getScheduleId()));
            return ResponseEntity.created(URI.create("/reservation/" + reservationId))
                    .build();
        }
        Reservation reservation = findOptionalReservation.get();
        reservationService.validateReservationOwner(reservation, member);
        Long reservationWaitingId = reservationWaitingService.create(reservation, member);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + reservationWaitingId))
                .build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readOwnReservationWaitings(@LoginMember UserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        List<ReservationWaitingResponse> reservationWaitingResponses = reservationWaitingService.findOwn(member)
                .stream()
                .map(reservationWaitingService::convertToReservationWaitingResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(reservationWaitingResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Member member = memberService.findById(userDetails.getId());
        ReservationWaiting reservationWaiting = reservationWaitingService.findById(id);
        reservationWaitingService.validateByMember(reservationWaiting, member);
        reservationWaitingService.delete(id);

        return ResponseEntity.noContent()
                .build();
    }
}
