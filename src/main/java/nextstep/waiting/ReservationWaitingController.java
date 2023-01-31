package nextstep.waiting;

import auth.LoginMember;
import auth.dto.UserDetails;
import nextstep.member.Member;
import nextstep.waiting.dto.ReservationWaitingRequest;
import nextstep.waiting.dto.ReservationWaitingCreatedResponse;
import nextstep.waiting.dto.ReservationWaitingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingProxyService reservationWaitingProxyService;

    public ReservationWaitingController(ReservationWaitingProxyService reservationWaitingProxyService) {
        this.reservationWaitingProxyService = reservationWaitingProxyService;
    }

    @PostMapping
    ResponseEntity createReservationWaiting(@LoginMember UserDetails userDetails, @RequestBody ReservationWaitingRequest request) {
        ReservationWaitingCreatedResponse response = reservationWaitingProxyService.makeReservation(Member.of(userDetails), request.getScheduleId());
        if (response.getWaiting()) {
            return ResponseEntity.created(URI.create("/reservation-waitings/" + response.getId())).build();
        }
        return ResponseEntity.created(URI.create("/reservations/" + response.getId())).build();
    }

    @GetMapping
    ResponseEntity getReservationWaitings(@LoginMember UserDetails userDetails) {
        List<ReservationWaitingResponse> responses = reservationWaitingProxyService.getReservationWaitings(Member.of(userDetails))
                .stream()
                .map(ReservationWaitingResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    ResponseEntity removeReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationWaitingProxyService.deleteById(Member.of(userDetails).getId(), id);

        return ResponseEntity.noContent().build();
    }
}
