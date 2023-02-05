package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.response.ReservationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    public final ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Reservation newReservation = reservationService.create(member, reservationRequest.getScheduleId());
        Long themeId = newReservation.getSchedule().getTheme().getId();
        LocalDate date = newReservation.getSchedule().getDate();
        return ResponseEntity.created(URI.create("/reservations?themeId=" + themeId + "&date=" + date)).build();
    }

    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@LoginMember Member member) {
        List<ReservationResponseDto> reservations = reservationService.getReservationsByMember(member)
                .stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approveReservation(@PathVariable long id) {
        reservationService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable long id, @LoginMember Member member) {
        reservationService.cancel(member, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cancel-approve")
    public ResponseEntity<Void> cancelApproveReservation(@PathVariable long id) {
        reservationService.cancelApprove(id);
        return ResponseEntity.ok().build();
    }
}
