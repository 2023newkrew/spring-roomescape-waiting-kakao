package roomwaiting.nextstep.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomwaiting.nextstep.reservation.service.ReservationAdminService;


@RestController
@RequestMapping("/reservations")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    public ReservationAdminController(ReservationAdminService reservationAdminService) {
        this.reservationAdminService = reservationAdminService;
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        reservationAdminService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel-approve")
    public ResponseEntity<Void> cancelApprove(@PathVariable Long id) {
        reservationAdminService.cancelApprove(id);
        return ResponseEntity.ok().build();
    }
}

