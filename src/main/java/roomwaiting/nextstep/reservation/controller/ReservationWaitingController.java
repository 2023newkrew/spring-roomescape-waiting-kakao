package roomwaiting.nextstep.reservation.controller;

import roomwaiting.auth.userdetail.UserDetails;
import java.net.URI;
import java.util.List;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
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

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<String> create(@LoginMember UserDetails member, @RequestBody ReservationRequest reservationRequest) {
        String waitingReservation = "/reservation-waitings/" +
                                    reservationWaitingService.create(new Member(member), reservationRequest);
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
