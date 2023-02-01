package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.response.ReservationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.cancelById(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@LoginMember Member member) {
        List<ReservationResponseDto> reservations = reservationService.getReservationsByMember(member)
                .stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }
}
