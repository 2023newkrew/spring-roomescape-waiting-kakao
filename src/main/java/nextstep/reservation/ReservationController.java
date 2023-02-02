package nextstep.reservation;

import auth.AuthenticationException;
import auth.LoginMember;
import auth.UserDetails;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import nextstep.member.MemberService;
import nextstep.reservation.dto.ReservationCreateDto;
import nextstep.reservation.dto.ReservationReadDto;
import nextstep.reservation.response.ReservationResponse;
import nextstep.reservation.response.ReservationWaitingResponse;
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

@RequiredArgsConstructor
@RestController
public class ReservationController {

    public final ReservationService reservationService;
    public final MemberService memberService;

    @PostMapping("/reservations")
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        ReservationCreateDto reservationCreateDto = reservationService.create(memberService.findByUserDetatils(userDetails), reservationRequest);
        Long id = reservationCreateDto.getId();
        if (reservationCreateDto.isReserved()) {
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        }
        return ResponseEntity.created(URI.create("/reservation-waitings/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<Reservation> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<List<ReservationResponse>> readMyReservations(@LoginMember UserDetails userDetails) {
        List<Reservation> reservations = reservationService.findReservationsByUserDetails(userDetails);
        List<ReservationReadDto> results = reservationService.classifyReservations(reservations, ReservationState.RESERVED);
        return ResponseEntity.ok().body(results.stream().map(ReservationReadDto::toReservationResponse).collect(Collectors.toList()));
    }

    @GetMapping("/reservation-waitings/mine")
    public ResponseEntity<List<ReservationWaitingResponse>> readReservationWaitings(@LoginMember UserDetails userDetails) {
        List<Reservation> reservations = reservationService.findReservationsByUserDetails(userDetails);
        List<ReservationReadDto> results = reservationService.classifyReservations(reservations, ReservationState.WAITING);
        return ResponseEntity.ok().body(results.stream().map(ReservationReadDto::toReservationWaitingResponse).collect(Collectors.toList()));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationResponse> readReservationById(@PathVariable Long id) {
        ReservationReadDto reservationReadDto = reservationService.findReservationById(id);
        return ResponseEntity.ok().body(reservationReadDto.toReservationResponse());
    }

    @GetMapping("/reservation-waitings/{id}")
    public ResponseEntity<ReservationWaitingResponse> readReservationWaitingById(@PathVariable Long id) {
        ReservationReadDto reservationReadDto = reservationService.findReservationById(id);
        return ResponseEntity.ok().body(reservationReadDto.toReservationWaitingResponse());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        int deletedRowCount = reservationService.cancelReservation(userDetails, id);

        return ResponseEntity.ok().body(Collections.singletonMap("deletedReservationCount", deletedRowCount));
    }

    @DeleteMapping("/reservation-waitings/{id}")
    public ResponseEntity deleteReservationWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        int deletedRowCount = reservationService.cancelReservation(userDetails, id);

        return ResponseEntity.ok().body(Collections.singletonMap("deletedReservationCount", deletedRowCount));
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
