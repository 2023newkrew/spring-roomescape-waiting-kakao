package nextstep.presentation.admin;

import lombok.RequiredArgsConstructor;
import nextstep.dto.request.TransitionReservationStatusRequest;
import nextstep.service.ReservationTransitionBatchService;
import nextstep.service.ReservationSalesStatisticsService;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/reservations")
@RestController
public class AdminReservationController {

    private final ReservationService reservationService;
    private final ReservationTransitionBatchService reservationTransitionBatchService;
    private final ReservationSalesStatisticsService reservationSalesStatisticsService;

    @PutMapping("/{reservationId}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable Long reservationId) {
        reservationService.approveReservation(reservationId);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{reservationId}/cancel-approve")
    public ResponseEntity<Void> approveCancelReservation(@PathVariable Long reservationId) {
        reservationService.approveCancelReservation(reservationId);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/{reservationId}/reject")
    public ResponseEntity<Void> rejectReservation(@PathVariable Long reservationId) {
        reservationService.rejectReservation(reservationId);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/batch/transition")
    public ResponseEntity<Void> transitReservations(@RequestBody TransitionReservationStatusRequest transitionReservationStatusRequest) {
        reservationTransitionBatchService.transitionReservationStatus(transitionReservationStatusRequest.getStatus());

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/batch/daily-sales")
    public ResponseEntity<Void> getDailySales() {
        reservationSalesStatisticsService.calculateSalesStatistics();

        return ResponseEntity.ok()
                .build();
    }

}
