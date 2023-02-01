package roomescape.nextstep.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.LoginMember;
import roomescape.auth.UserDetails;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    public final ReservationService reservationService;

    @PostMapping({"/reservations", "/reservations-waitings"})
    public ResponseEntity<Reservation> createReservation(@LoginMember UserDetails member, @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.create(member.getUsername(), reservationRequest);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
                    .build();
        }
        return ResponseEntity.created(URI.create("/reservations-waitings/" + reservation.getId()))
                .build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findReservationsByThemeIdAndDateAndStatus(themeId, date, ReservationStatus.CONFIRMED);
        return ResponseEntity.ok()
                .body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationService.deleteById(member.getUsername(), id, ReservationStatus.CONFIRMED);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> findMyReservations(@LoginMember UserDetails member) {
        return ResponseEntity.ok(
                reservationService.findReservationsByUsername(member.getUsername())
        );
    }

    @GetMapping("/reservations-waitings/mine")
    public ResponseEntity<List<ReservationWaiting>> findMyReservationsWaitings(@LoginMember UserDetails member) {
        return ResponseEntity.ok(
                reservationService.findWaitingReservationsByUsername(member.getUsername())
        );
    }

    @DeleteMapping("/reservations-waitings/{id}")
    public ResponseEntity<Void> deleteReservationWaitings(@LoginMember UserDetails member, @PathVariable Long id) {
        reservationService.deleteById(member.getUsername(), id, ReservationStatus.WAITING);
        return ResponseEntity.noContent()
                .build();
    }
}
