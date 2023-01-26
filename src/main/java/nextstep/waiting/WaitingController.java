package nextstep.waiting;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.List;
import nextstep.member.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WaitingController {

    public final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

//    @PostMapping("/reservation-waitings")
//    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody int scheduleId) {
//        Long id = waitingService.create(member, waitingRequest);
//        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
//    }

//    @GetMapping("/reservations")
//    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
//        List<Waiting> results = waitingService.findAllByThemeIdAndDate(themeId, date);
//        return ResponseEntity.ok().body(results);
//    }
//
//    @DeleteMapping("/reservations/{id}")
//    public ResponseEntity deleteReservation(@LoginMember Member member, @PathVariable Long id) {
//        waitingService.deleteById(member, id);
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity onException(Exception e) {
//        return ResponseEntity.badRequest().build();
//    }
//
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity onAuthenticationException(AuthenticationException e) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
}
