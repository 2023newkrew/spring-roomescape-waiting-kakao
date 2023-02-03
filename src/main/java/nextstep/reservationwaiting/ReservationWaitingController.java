package nextstep.reservationwaiting;

import auth.config.LoginMember;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.exception.CanMakeReservationException;
import nextstep.member.Member;
import nextstep.reservation.ReservationService;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingResponse;
import nextstep.reservationwaiting.dto.ReservationWaitingResponseWithCurrentWaitNum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ReservationWaitingController {
    private final ReservationWaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService,
                                        ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation-waitings")
    public ResponseEntity<Void> create(@LoginMember Member member,
                                       @RequestBody ReservationWaitingRequest reservationWaitingRequest) {
        boolean isDuplicate = reservationService.isDuplicateByScheduleId(reservationWaitingRequest.getScheduleId());
        if (isDuplicate) {
            Long id = reservationWaitingService.create(member, reservationWaitingRequest);
            return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
        }
        throw new CanMakeReservationException(member, reservationWaitingRequest);

    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> findByMemberId(@LoginMember Member member) {
        return ResponseEntity.ok(
                reservationWaitingService.findAllByMemberId(member.getId()).stream().map(ReservationWaitingResponse::of)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/reservation-waitings/{id}")
    public ResponseEntity<ReservationWaitingResponseWithCurrentWaitNum> findById(@LoginMember Member member,
                                                                                 @PathVariable Long id) {
        ReservationWaiting reservationWaiting = reservationWaitingService.findById(member, id);
        return ResponseEntity.ok(
                new ReservationWaitingResponseWithCurrentWaitNum(reservationWaiting,
                        reservationWaitingService.findCurrentWaitNum(reservationWaiting.getSchedule().getId(),
                                reservationWaiting.getWaitNum())));
    }

    @DeleteMapping("/admin/reservation-waitings/{id}")
    public ResponseEntity<Void> deleteById(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity<Void> dropById(@LoginMember Member member, @PathVariable Long id) {
        reservationWaitingService.dropById(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CanMakeReservationException.class)
    public ResponseEntity<Void> onCanMakeReservationException(CanMakeReservationException e) {
        Long id = reservationService.create(e.getMember(), e.getRequest());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

}
