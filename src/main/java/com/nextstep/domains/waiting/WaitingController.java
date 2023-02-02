package com.nextstep.domains.waiting;

import com.nextstep.domains.global.exceptions.DuplicateEntityException;
import com.nextstep.domains.member.entities.MemberEntity;
import com.nextstep.domains.waiting.dtos.WaitingRequest;
import com.nextstep.domains.waiting.entities.WaitingEntity;
import com.nextstep.infrastructures.web.UserContext;
import com.authorizationserver.domains.authorization.entities.UserDetailsEntity;
import com.nextstep.domains.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservation-waitings")
public class WaitingController {
    private final WaitingService reservationWaitingService;
    private final ReservationService reservationService;

    public WaitingController(WaitingService reservationWaitingService, ReservationService reservationService) {
        this.reservationWaitingService = reservationWaitingService;
        this.reservationService = reservationService;
    }


    @PostMapping
    ResponseEntity createReservationWaiting(@UserContext UserDetailsEntity userDetails, @RequestBody WaitingRequest request) {
        try {
            Long id = reservationService.create(MemberEntity.of(userDetails), request.getScheduleId());
            return ResponseEntity.created(URI.create("/reservations/" + id)).build();
        } catch (DuplicateEntityException e) {
            Long id = reservationWaitingService.create(MemberEntity.of(userDetails), request.getScheduleId());
            return ResponseEntity.created(URI.create("/reservation_waitings/" + id)).build();
        }

    }

    @GetMapping
    ResponseEntity getReservationWaitings(@UserContext UserDetailsEntity userDetails) {
        List<WaitingEntity> reservationWaitings = reservationWaitingService.getReservationWaitings(MemberEntity.of(userDetails));

        return ResponseEntity.ok(reservationWaitings);
    }

    @DeleteMapping("/{id}")
    ResponseEntity removeReservationWaiting(@UserContext UserDetailsEntity userDetails, @PathVariable Long id) {
        reservationWaitingService.deleteById(MemberEntity.of(userDetails).getId(), id);

        return ResponseEntity.noContent().build();
    }
}
