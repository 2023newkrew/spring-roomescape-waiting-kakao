package nextstep.reservation.controller;

import lombok.RequiredArgsConstructor;
import auth.support.AuthenticationPrincipal;
import auth.support.LoginRequired;
import nextstep.member.model.Member;
import nextstep.reservation.model.CreateReservationRequest;
import nextstep.reservation.model.Reservation;
import nextstep.reservation.model.ReservationRequest;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    public final ReservationService reservationService;

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> createReservation(@AuthenticationPrincipal Member member, @Valid @RequestBody ReservationRequest reservationRequest) {
        CreateReservationRequest createReservationRequest = CreateReservationRequest.to(reservationRequest, member.getMemberName());
        Long id = reservationService.create(createReservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<Reservation>> readReservations(@AuthenticationPrincipal Member member, @RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date, member);
        return ResponseEntity.ok().body(results);
    }

    @LoginRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        reservationService.deleteById(id, member);

        return ResponseEntity.noContent().build();
    }
}
