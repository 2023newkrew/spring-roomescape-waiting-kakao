package nextstep.web.reservation.controller;

import auth.login.LoginMember;
import nextstep.web.member.domain.Member;
import nextstep.web.reservation.domain.Reservation;
import nextstep.web.reservation.dto.ReservationRequest;
import nextstep.web.reservation.service.ReservationService;
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
    public ResponseEntity<Void> createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<Reservation>> readAllReservation(@LoginMember Member member){
        List<Reservation> results = reservationService.findAllByMember(member);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }
}
