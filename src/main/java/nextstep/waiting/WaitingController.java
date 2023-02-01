package nextstep.waiting;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.waiting.dto.response.ReservationWaitingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping("/reservation-waitings")
    public ResponseEntity createReservationWaiting(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = waitingService.createWaiting(member, reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        waitingService.deleteWaitingById(member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponseDto>> createReservationWaiting(@LoginMember Member member) {
        List<ReservationWaitingResponseDto> dtos = waitingService.getReservationWaitingsByMember(member);
        return ResponseEntity.ok(dtos);
    }
}
