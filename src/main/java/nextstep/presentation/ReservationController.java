package nextstep.presentation;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import nextstep.domain.reservation.Reservation;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(loginMember.getId(), reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + id))
                .build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> findMyReservations(@AuthenticationPricipal LoginMember loginMember){
        List<ReservationResponse> results = reservationService.findMyReservations(loginMember.getId());

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity deleteReservation(@AuthenticationPricipal LoginMember loginMember, @PathVariable Long reservationId) {
        reservationService.deleteById(loginMember.getId(), reservationId);

        return ResponseEntity.noContent()
                .build();
    }

}
