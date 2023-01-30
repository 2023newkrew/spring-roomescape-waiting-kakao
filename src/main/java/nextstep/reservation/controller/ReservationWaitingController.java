package nextstep.reservation.controller;

import auth.AuthenticationException;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.service.ReservationService;
import nextstep.reservation.service.ReservationWaitingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import auth.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.ReservationRequest;

@Validated
@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity create(@LoginMember UserDetails member, @RequestBody @Valid ReservationRequest reservationRequest) {
        String waitingReservation = "/reservation-waitings/" +
                                    reservationWaitingService.create(new Member(member), reservationRequest);
        return ResponseEntity.created(URI.create(waitingReservation)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaiting>> lookUp(@LoginMember UserDetails member) {
        List<ReservationWaiting> reservationWaitingList = reservationWaitingService.lookUp(new Member(member));
        return ResponseEntity.ok().body(reservationWaitingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember UserDetails member, @PathVariable @NotNull @Min(1L) Long id){
        reservationWaitingService.delete(new Member(member), id);
        return ResponseEntity.noContent().build();
    }
}
