package nextstep.reservation.controller;

import auth.model.MemberDetails;
import auth.support.AuthenticationPrincipal;
import auth.support.LoginRequired;
import lombok.RequiredArgsConstructor;
import nextstep.reservation.model.CreateReservation;
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
    @GetMapping("/mine")
    public ResponseEntity<List<Reservation>> getMyReservationWaiting(@AuthenticationPrincipal MemberDetails userDetail){
        String memberName = userDetail.getMemberName();
        List<Reservation> reservations = reservationService.findByMemberId(memberName, memberName);
        return ResponseEntity.ok().body(reservations);
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> createReservation(@AuthenticationPrincipal MemberDetails memberDetails
            , @Valid @RequestBody ReservationRequest reservationRequest) {
        CreateReservation createReservation = CreateReservation.from(reservationRequest, memberDetails.getMemberName());
        Long id = reservationService.create(createReservation);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @LoginRequired
    @GetMapping
    public ResponseEntity<List<Reservation>> readReservations(@AuthenticationPrincipal MemberDetails memberDetails
            , @RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date, memberDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @LoginRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@AuthenticationPrincipal MemberDetails memberDetails
            , @PathVariable Long id) {
        reservationService.deleteById(id, memberDetails.getId());

        return ResponseEntity.noContent().build();
    }
}
