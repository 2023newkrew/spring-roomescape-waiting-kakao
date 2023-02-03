package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.login.UserDetails;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation-waitings")
@RequiredArgsConstructor
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(@LoginMember final UserDetails userDetails,
        @RequestBody final ReservationWaitingRequest reservationWaitingRequest) {
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
    public ResponseEntity<List<ReservationWaitingResponse>> readOwnReservationWaitings(@LoginMember final UserDetails userDetails) {
        Member member = memberService.findById(userDetails.getId());
        List<ReservationWaitingResponse> reservationWaitingResponses = reservationWaitingService.findOwn(member)
            .stream()
            .map(reservationWaitingService::convertToReservationWaitingResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(reservationWaitingResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginMember final UserDetails userDetails, @PathVariable final Long id) {
        Member member = memberService.findById(userDetails.getId());
        ReservationWaiting reservationWaiting = reservationWaitingService.findById(id);
        reservationWaitingService.validateByMember(reservationWaiting, member);
        reservationWaitingService.delete(id);

        return ResponseEntity.noContent()
            .build();
    }
}
