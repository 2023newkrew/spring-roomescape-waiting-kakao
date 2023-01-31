package nextstep.web.reservation_waiting.controller;

import auth.login.LoginMember;
import auth.login.MemberDetail;
import lombok.RequiredArgsConstructor;
import nextstep.web.member.domain.Member;
import nextstep.web.reservation_waiting.dto.ReservationWaitingRequest;
import nextstep.web.reservation_waiting.dto.ReservationWaitingResponse;
import nextstep.web.reservation_waiting.service.ReservationWaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {

    public final ReservationWaitingService reservationWaitingService;

    @PostMapping
    public ResponseEntity<Void> createReservationWaiting(
            @LoginMember MemberDetail member,
            @RequestBody ReservationWaitingRequest reservationWaitingRequest
    ) {
        String location = reservationWaitingService.create(Member.fromMemberDetail(member), reservationWaitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readMyReservationWaiting(@LoginMember MemberDetail member) {
        List<ReservationWaitingResponse> results = reservationWaitingService.readMine(Member.fromMemberDetail(member));

        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationWaiting(@LoginMember MemberDetail member, @PathVariable Long id) {
        reservationWaitingService.deleteById(Member.fromMemberDetail(member), id);

        return ResponseEntity.noContent().build();
    }
}
