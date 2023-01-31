package nextstep.reservation;

import auth.LoginMember;
import auth.dto.UserDetails;
import nextstep.member.Member;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createcReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Member member = Member.of(userDetails);
        Long id = reservationService.create(Member.of(userDetails), reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationService.findAllByMemberId(Member.of(userDetails).getId())
                .stream()
                .map(ReservationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.deleteById(Member.of(userDetails), id);

        return ResponseEntity.noContent().build();
    }
}
