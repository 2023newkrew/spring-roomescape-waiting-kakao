package nextstep.presentation.admin;

import lombok.RequiredArgsConstructor;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {

    private final ReservationService reservationService;

    @PutMapping("/{reservationId}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable Long reservationId) {
        reservationService.approveReservation(reservationId);

        return ResponseEntity.ok()
                .build();
    }

}
