package roomescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.annotation.JWTBearerTokenSubject;
import roomescape.dto.ReservationControllerGetResponse;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.exception.AuthorizationException;
import roomescape.exception.NotExistReservationException;
import roomescape.repository.ReservationRepository;

import javax.validation.Valid;


@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository repository;

    @PostMapping(value = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> createReservation(@JWTBearerTokenSubject String subject, @Valid @RequestBody ReservationsControllerPostBody body) {
        var id = repository.insert(body.getName(), body.getDate(), body.getTime(), body.getThemeId(), Long.parseLong(subject));
        if (id.isEmpty()) {
            throw new AlreadyExistReservationException(body.getDate(), body.getTime());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/reservations/%d", id.get()))
                             .build();
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReservationControllerGetResponse> findReservation(@PathVariable Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new NotExistReservationException(id);
        }
        var getReservation = reservation.get();
        return ResponseEntity.status(HttpStatus.OK)
                             .body(new ReservationControllerGetResponse(
                                     getReservation.getId(),
                                     getReservation.getDate(),
                                     getReservation.getTime(),
                                     getReservation.getName(),
                                     getReservation.getTheme().getId(),
                                     getReservation.getTheme().getName(),
                                     getReservation.getTheme().getDesc(),
                                     getReservation.getTheme().getPrice()
                             ));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> deleteReservation(@JWTBearerTokenSubject String subject, @PathVariable Long id) {
        var reservation = repository.selectById(id);
        if (reservation.isEmpty()) {
            throw new NotExistReservationException(id);
        }
        if (reservation.get().getMemberId() != Long.parseLong(subject)) {
            throw new AuthorizationException();
        }
        repository.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
