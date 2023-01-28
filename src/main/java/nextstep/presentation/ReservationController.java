package nextstep.presentation;

import auth.presentation.argumentresolver.LoginMember;
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
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("reservations/mine")
    public ResponseEntity<List<ReservationResponse>> findMyReservations(@LoginMember UserDetails userDetails){
        List<ReservationResponse> results = reservationService.findMyReservations(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createReservationWaiting(userDetails.getId(), reservationRequest);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("reservation-waitings/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long reservationWaitingId){
        reservationService.deleteReservationWaitingById(userDetails.getId(), reservationWaitingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity findMyReservationsWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaitingResponse> reservationWaitings = reservationService.findMyReservationWaitings(userDetails.getId());
        return ResponseEntity.ok(reservationWaitings);
    }

}
