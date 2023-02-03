package nextstep.controller;

import auth.domain.UserDetails;
import auth.support.template.LoginMember;
import nextstep.domain.ReservationStatus;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;

    public AdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PatchMapping("/reservations/{id}/approve")
    public ResponseEntity<?> approveResevation(@LoginMember UserDetails userDetails, @PathVariable long id) {
        reservationService.approveReservationById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/{id}/cancel-approve")
    public ResponseEntity<?> cancelApproveReservation(@LoginMember UserDetails userDetails, @PathVariable long id) {
        reservationService.updateStatusById(id, ReservationStatus.CANCELLED);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservations/{id}/cancel-reject")
    public ResponseEntity<?> cancelDenyReservation(@LoginMember UserDetails userDetails, @PathVariable long id) {
        reservationService.updateStatusById(id, ReservationStatus.REJECTED);
        return ResponseEntity.ok().build();
    }
}
