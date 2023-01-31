package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.support.DuplicateEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> createReservation(@LoginMember Long memberId, @RequestBody ReservationRequest reservationRequest) {
        Long reservationId = reservationService.create(memberId, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + reservationId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationResponse> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@LoginMember Long memberId, @PathVariable Long id) {
        reservationService.deleteById(memberId, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> mine(@LoginMember Long memberId) {
        return ResponseEntity.ok(reservationService.findByMemberId(memberId));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Void> onDuplicateEntityException(DuplicateEntityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
