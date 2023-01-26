package nextstep.waiting;

import auth.LoginMember;
import auth.MemberDetails;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private ReservationWaitingService reservationWaitingService;
    public ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@LoginMember MemberDetails memberDetails,
                                            @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        String location = "/reservations/";
        if (reservationService.hasReservation(reservationWaitingRequest.getScheduleId())) {
            location = "/reservation-waitings/";
        }

        Long id = reservationWaitingService.create(Member.from(memberDetails), reservationWaitingRequest);
        return ResponseEntity.created(URI.create(location + id)).build();
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
}
