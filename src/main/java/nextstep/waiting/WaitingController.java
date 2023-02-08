package nextstep.waiting;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.waiting.dto.response.ReservationWaitingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation-waitings")
public class WaitingController {

    private final WaitingService waitingService;
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity createReservationWaiting(@LoginMember Member member, @RequestBody ReservationRequest reservationRequest) {
        Long id = waitingService.createWaiting(member, reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservation-waitings/mine")).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity cancelReservationWaiting(@LoginMember Member member, @PathVariable Long id) {
        waitingService.cancelWaitingById(reservationService::cancel, member, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationWaitingResponseDto>> createReservationWaiting(@LoginMember Member member) {
        List<ReservationWaitingResponseDto> dtos = waitingService.getReservationWaitingsByMember(member);
        return ResponseEntity.ok(dtos);
    }
}
