package nextstep.reservation.controller;

import auth.annotation.LoginMember;
import auth.domain.UserDetails;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import nextstep.member.Member;
import nextstep.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveReservation(@LoginMember UserDetails member,
                                                   @PathVariable @NotNull @Min(1L) Long id) {
        reservationService.approveReservation(new Member(member), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/refuse")
    public ResponseEntity<Void> refuseReservation(@LoginMember UserDetails member,
                                                  @PathVariable @NotNull @Min(1L) Long id) {
        reservationService.refuseReservation(new Member(member), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel-approve")
    public ResponseEntity<Void> approveCancelOfReservation(@LoginMember UserDetails member,
                                                           @PathVariable @NotNull @Min(1L) Long id) {
        reservationService.approveCancelOfReservation(new Member(member), id);
        return ResponseEntity.ok().build();
    }
}
