package nextstep.presentation;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import nextstep.domain.reservation.Reservation;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.CreateReservationResponse;
import nextstep.dto.response.ReservationResponse;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/reservations")
@RestController
public class ReservationController {

    private final static String RESERVATION_URL_PREFIX = "/reservations/";
    private final static String RESERVATION_WAITING_URL_PREFIX = "/reservation-waitings/";

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        CreateReservationResponse createReservationResponse = reservationService.createReservationOrReservationWaiting(loginMember.getId(), reservationRequest);
        String urlPrefix = createReservationResponse.isReservationCreated() ? RESERVATION_URL_PREFIX : RESERVATION_WAITING_URL_PREFIX;

        return ResponseEntity.created(URI.create(urlPrefix + createReservationResponse.getId()))
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
