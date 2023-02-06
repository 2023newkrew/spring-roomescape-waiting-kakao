package com.nextstep.interfaces.waiting;

import com.authorizationserver.infrastructures.jwt.TokenData;
import com.nextstep.infrastructures.web.UseContext;
import com.nextstep.interfaces.exceptions.ErrorMessageType;
import com.nextstep.interfaces.exceptions.ReservationException;
import com.nextstep.interfaces.exceptions.ScheduleException;
import com.nextstep.interfaces.reservation.dtos.ReservationRequest;
import com.nextstep.interfaces.reservation.dtos.ReservationResponse;
import com.nextstep.domains.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import com.nextstep.interfaces.schedule.dtos.ScheduleResponse;
import com.nextstep.domains.schedule.ScheduleService;
import com.nextstep.interfaces.waiting.dtos.WaitingRequest;
import com.nextstep.interfaces.waiting.dtos.WaitingResponse;
import com.nextstep.domains.waiting.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
@RestController
public class WaitingController {

    private static final String WAITING_PATH = "/reservation-waitings/";

    private final WaitingService service;

    private final ReservationService reservationService;

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> create(
            @UseContext TokenData tokenData,
            @RequestBody WaitingRequest request) {
        URI location;
        ScheduleResponse scheduleResponse = scheduleService.getById(request.getScheduleId());
        validateSchedule(scheduleResponse);
        try {
            ReservationRequest reservationRequest = new ReservationRequest(request.getScheduleId());
            ReservationResponse reservation = reservationService.create(tokenData.getId(), reservationRequest, scheduleResponse);
            location = URI.create("/reservations/" + reservation.getId());
        }
        catch (ReservationException e) {
            if (reservationService.existsByMemberIdAndScheduleId(tokenData.getId(), request.getScheduleId())) {
                throw new ReservationException(ErrorMessageType.RESERVATION_CONFLICT);
            }
            WaitingResponse waiting = service.create(tokenData.getId(), request);
            location = URI.create(WAITING_PATH + waiting.getId());
        }
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{waiting_id}")
    public ResponseEntity<WaitingResponse> getById(@PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.getById(waitingId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<WaitingResponse>> getByMemberId(@UseContext TokenData tokenData) {
        return ResponseEntity.ok(service.getByMemberId(tokenData.getId()));
    }

    @DeleteMapping("/{waiting_id}")
    public ResponseEntity<Boolean> deleteById(
            @UseContext TokenData tokenData,
            @PathVariable("waiting_id") Long waitingId) {
        return ResponseEntity.ok(service.deleteById(tokenData.getId(), waitingId));
    }

    private void validateSchedule(ScheduleResponse schedule) {
        if (Objects.isNull(schedule)) {
            throw new ScheduleException(ErrorMessageType.SCHEDULE_NOT_EXISTS);
        }
    }
}
