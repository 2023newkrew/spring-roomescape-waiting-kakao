package nextstep.reservation.controller;

import auth.support.AuthenticationException;
import auth.support.LoginMember;
import nextstep.member.service.MemberService;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.reservation.service.ReservationService;
import nextstep.reservation.domain.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember Long memberId, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(memberService.findById(memberId), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@LoginMember Long memberId) {
        List<ReservationResponse> results = reservationService.findAllByMemberId(memberService.findById(memberId));
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember Long memberId, @PathVariable Long id) {
        reservationService.deleteById(memberService.findById(memberId), id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
