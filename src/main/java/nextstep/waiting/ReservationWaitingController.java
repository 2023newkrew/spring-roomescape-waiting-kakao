package nextstep.waiting;

import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import nextstep.support.DuplicateEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }


    @PostMapping
    ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest request) {
        try {
            Long id = reservationService.create(Member.of(userDetails), request.getScheduleId());
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        } catch (DuplicateEntityException e) {
            Long id = reservationWaitingService.create(Member.of(userDetails), request.getScheduleId());
            return ResponseEntity.created(URI.create("/reservation_waitings/" + id)).build();
        }

    }

    @GetMapping
    ResponseEntity getReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaiting> reservationWaitings = reservationWaitingService.getReservationWaitings(Member.of(userDetails));

        return ResponseEntity.ok(reservationWaitings);
    }

    @DeleteMapping("/{id}")
    ResponseEntity removeReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationWaitingService.deleteById(Member.of(userDetails).getId(), id);

        return ResponseEntity.noContent().build();
    }
}
