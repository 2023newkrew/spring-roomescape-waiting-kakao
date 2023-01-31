package nextstep.waiting;

import auth.annotation.LoginMember;
import auth.dto.MemberDetails;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    public static final String RESERVATION_WAITINGS_URI = "reservation-waitings";
    public static final String RESERVATIONS_URI = "reservations";

    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@LoginMember MemberDetails memberDetails,
                                            @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        boolean isReservationWaiting = reservationService.hasReservation(reservationWaitingRequest.getScheduleId());
        Long id = reservationWaitingService.create(Member.from(memberDetails), reservationWaitingRequest);
        URI uri = generateLocationUri(id, isReservationWaiting);
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/mine")
    public ResponseEntity findAllReservationsOfMember(@LoginMember MemberDetails memberDetails) {
        List<ReservationWaiting> results = reservationWaitingService.findByMemberId(Member.from(memberDetails));
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember MemberDetails memberDetails, @PathVariable Long id) {
        reservationWaitingService.deleteById(Member.from(memberDetails), id);
        return ResponseEntity.noContent().build();
    }

    private URI generateLocationUri(Long id, boolean isReservationWaiting) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("/{reservation}/{id}");
        if (isReservationWaiting) {
            return uriComponentsBuilder.buildAndExpand(RESERVATION_WAITINGS_URI, id).toUri();
        }
        return uriComponentsBuilder.buildAndExpand(RESERVATIONS_URI, id).toUri();
    }
}
