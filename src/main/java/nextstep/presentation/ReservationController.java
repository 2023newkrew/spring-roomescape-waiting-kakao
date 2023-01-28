package nextstep.presentation;

import auth.dto.request.LoginMember;
import auth.presentation.argumentresolver.AuthenticationPricipal;
import auth.domain.UserDetails;
import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.dto.request.ReservationRequest;
import nextstep.dto.response.ReservationResponse;
import nextstep.dto.response.ReservationWaitingResponse;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(loginMember.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("reservations/mine")
    public ResponseEntity<List<ReservationResponse>> findMyReservations(@AuthenticationPricipal LoginMember loginMember){
        List<ReservationResponse> results = reservationService.findMyReservations(loginMember.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@AuthenticationPricipal LoginMember loginMember, @PathVariable Long id) {
        reservationService.deleteById(loginMember.getId(), id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createReservationWaiting(@AuthenticationPricipal LoginMember loginMember, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createReservationWaiting(loginMember.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("reservation-waitings/{reservationWaitingId}")
    public ResponseEntity<Void> deleteReservationWaiting(@AuthenticationPricipal LoginMember loginMember, @PathVariable Long reservationWaitingId){
        reservationService.deleteReservationWaitingById(loginMember.getId(), reservationWaitingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity findMyReservationsWaitings(@AuthenticationPricipal LoginMember loginMember) {
        List<ReservationWaitingResponse> reservationWaitings = reservationService.findMyReservationWaitings(loginMember.getId());
        return ResponseEntity.ok(reservationWaitings);
    }

}
