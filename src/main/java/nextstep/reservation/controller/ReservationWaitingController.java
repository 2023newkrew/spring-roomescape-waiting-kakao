package nextstep.reservation.controller;

import java.net.URI;
import java.util.List;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationWaiting;
import nextstep.reservation.service.ReservationService;
import nextstep.reservation.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import auth.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.ReservationRequest;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity create(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        String waitingReservation = reservationWaitingService.create(member, reservationRequest);
        return ResponseEntity.created(URI.create(waitingReservation)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaiting>> lookUp(@LoginMember Member member) {
        List<ReservationWaiting> reservationWaitingList = reservationWaitingService.lookUp(member);
        return ResponseEntity.ok().body(reservationWaitingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember Member member, @PathVariable Long id){
        reservationWaitingService.delete(member, id);
        return ResponseEntity.noContent().build();
    }
}
