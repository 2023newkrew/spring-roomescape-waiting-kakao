package com.nextstep.domains.reservation;

import com.nextstep.domains.reservation.dtos.ReservationRequest;
import com.nextstep.infrastructures.web.UserContext;
import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.reservation.dtos.ReservationResponse;
import com.nextstep.domains.reservation.entities.ReservationEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReservationController {

    public final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity createcReservation(@UserContext UserDetailsEntity userDetails, @RequestBody ReservationRequest reservationRequest) {
        MemberEntity member = MemberEntity.of(userDetails);
        Long id = reservationService.create(MemberEntity.of(userDetails), reservationRequest.getScheduleId());
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @GetMapping("/reservations")
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationEntity> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity readMyReservations(@UserContext UserDetailsEntity userDetails) {
        List<ReservationResponse> results = reservationService.findAllByMemberId(MemberEntity.of(userDetails).getId())
                .stream()
                .map(ReservationResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity deleteReservation(@UserContext UserDetailsEntity userDetails, @PathVariable Long id) {
        reservationService.deleteById(MemberEntity.of(userDetails), id);

        return ResponseEntity.noContent().build();
    }
}
