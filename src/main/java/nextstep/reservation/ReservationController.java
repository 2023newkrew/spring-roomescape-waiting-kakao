package nextstep.reservation;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.response.ReservationResponseDto;
import nextstep.reservation.dto.response.ReservationWaitingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ReservationController {

    public final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(member, reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteById(member, id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.createWaiting(member, reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        reservationService.deleteWaitingById(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@LoginMember Member member) {
        List<ReservationResponseDto> reservations = reservationService.getReservationsByMember(member)
                .stream()
                .map(ReservationResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponseDto>> createReservationWaiting(@LoginMember Member member) {
        List<ReservationWaitingResponseDto> dtos = reservationService.getReservationWaitingsByMember(member);
        return ResponseEntity.ok(dtos);
    }
}
