package nextstep.reservation_waiting;

import auth.LoginMember;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

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
        reservationService.validateByMember(reservation, member);
        Long reservationWaitingId = reservationWaitingService.create(reservation, member);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + reservationWaitingId))
                .build();
    }
}
