package nextstep.reservationwaiting;

import auth.config.LoginMember;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation-waitings")
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService,
                                        ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@LoginMember Member member,
                                       @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        boolean isDuplicate = reservationService.isDuplicateByScheduleId(reservationWaitingRequest.getScheduleId());
        if (isDuplicate) {
            Long id = reservationWaitingService.create(member, reservationWaitingRequest);
            return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
        }
        Long id = reservationService.create(member, new ReservationRequest(reservationWaitingRequest.getScheduleId()));
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("mine")
    public ResponseEntity<List<ReservationWaitingResponse>> findByMemberId(@LoginMember Member member) {
        return ResponseEntity.ok(
                reservationWaitingService.findAllByMemberId(member).stream().map(ReservationWaitingResponse::of)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
