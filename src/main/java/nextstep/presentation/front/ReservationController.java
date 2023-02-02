package nextstep.presentation.front;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
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
    public ResponseEntity<Void> createReservation(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        CreateReservationResponse createReservationResponse = reservationService.createReservationOrReservationWaiting(loginMember.getId(), reservationRequest);
        String urlPrefix = createReservationResponse.isReservationCreated() ? RESERVATION_URL_PREFIX : RESERVATION_WAITING_URL_PREFIX;

        return ResponseEntity.created(URI.create(urlPrefix + createReservationResponse.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationResponse> results = reservationService.findAllByThemeIdAndDate(themeId, date);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> findMyReservations(@AuthenticationPricipal LoginMember loginMember){
        List<ReservationResponse> results = reservationService.findMyReservations(loginMember.getId());

        return ResponseEntity.ok(results);
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);

        return ResponseEntity.ok()
                .build();
    }


}
