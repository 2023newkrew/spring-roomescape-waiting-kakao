package roomescape.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.ReservationControllerGetResponse;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;
import roomescape.exception.AlreadyExistReservationException;
import roomescape.exception.NotExistReservationException;
import roomescape.repository.ReservationRepository;

import javax.validation.Valid;


@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationDAO;

    public ReservationController(ReservationRepository reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @PostMapping(value = "", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationsControllerPostBody body) {
        var id = reservationDAO.addReservation(
                new Reservation(
                        null, body.getDate(), body.getTime(), body.getName(),
                        new Theme(body.getThemeName(), body.getThemeDesc(), body.getThemePrice())
                )
        );
        if (id.isEmpty()) {
            throw new AlreadyExistReservationException(body.getDate(), body.getTime());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                             .header("Location", String.format("/reservations/%d", id.get()))
                             .build();
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReservationControllerGetResponse> showReservation(@PathVariable Long id) {
        var reservation = reservationDAO.findReservation(id);
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
                                     getReservation.getTheme().getName(),
                                     getReservation.getTheme().getDesc(),
                                     getReservation.getTheme().getPrice()
                             ));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json;charset=utf-8")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        var affectedRow = reservationDAO.deleteReservation(id);
        if (affectedRow == 0) {
            throw new NotExistReservationException(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
