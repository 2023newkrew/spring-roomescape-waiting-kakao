package roomwaiting.nextstep.reservation.controller;

import roomwaiting.auth.userdetail.UserDetails;
import java.net.URI;
import java.util.List;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.reservation.service.ReservationService;
import roomwaiting.nextstep.reservation.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomwaiting.auth.principal.LoginMember;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<String> create(@LoginMember UserDetails member, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationWaitingService.create(new Member(member), reservationRequest);
        if (id == null){
            Long reserveId = reservationService.create(new Member(member), reservationRequest);
            String reservation = "/reservations/" + reserveId;
            return ResponseEntity.created(URI.create(reservation)).body("Location: " + reservation);
        }
        String waitingReservation = "/reservation-waitings/" + id;
        return ResponseEntity.created(URI.create(waitingReservation)).body("Location: " + waitingReservation);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaiting>> lookUp(@LoginMember UserDetails member) {
        List<ReservationWaiting> reservationWaitingList = reservationWaitingService.lookUp(new Member(member));
        return ResponseEntity.ok().body(reservationWaitingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember UserDetails member, @PathVariable Long id){
        reservationWaitingService.delete(new Member(member), id);
        return ResponseEntity.noContent().build();
    }

}
