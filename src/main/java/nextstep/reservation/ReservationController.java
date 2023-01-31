package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservationwaiting.ReservationWaitingService;
import nextstep.schedule.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    public final ReservationService reservationService;
    public final MemberService memberService;
    public final ReservationWaitingService reservationWaitingService;

    public ReservationController(ReservationService reservationService, MemberService memberService, ReservationWaitingService reservationWaitingService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(memberService.findByUserDetatils(userDetails), reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@LoginMember UserDetails userDetails) {
        List<Reservation> results = reservationService.findByMemberId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        Schedule schedule = reservationService.deleteByIdAndGetSchedule(memberService.findByUserDetatils(userDetails), id);
        Long topPriorityMemberId = reservationWaitingService.findTopPriorityMemberIdBySchedule(schedule);
        if (topPriorityMemberId == null) {
            return ResponseEntity.noContent().build();
        }

        Member topPriorityMember = memberService.findById(topPriorityMemberId);
        Long newId = reservationService.create(topPriorityMember, new ReservationRequest(schedule.getId()));

        return ResponseEntity.created(URI.create("/reservations/" + newId)).build();
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
