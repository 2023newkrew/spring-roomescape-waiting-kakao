package nextstep.reservationwaiting;

import auth.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.ReservationRequest;
import nextstep.reservation.ReservationService;
import nextstep.support.DuplicateEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Void> create(@LoginMember Member member, @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        try {
            Long id = reservationService.create(member, new ReservationRequest(reservationWaitingRequest.getScheduleId()));
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        } catch (DuplicateEntityException e) {
            Long id = reservationWaitingService.create(member, reservationWaitingRequest);
            return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
        }
    }

    @GetMapping("mine")
    public ResponseEntity<List<ReservationWaitingResponse>> findByMemberId(@LoginMember Member member) {
        return ResponseEntity.ok(reservationWaitingService
                .findAllByMemberId(member)
                .stream()
                .map(ReservationWaitingResponse::new)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
