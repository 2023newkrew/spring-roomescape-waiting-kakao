package nextstep.controller;

import auth.domain.persist.UserDetails;
import auth.domain.template.LoginMember;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.persist.Reservation;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(userDetails, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/mine")
    public ResponseEntity getReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationService.findAllByUserId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.deleteById(userDetails, id);
        return ResponseEntity.noContent().build();
    }

}
