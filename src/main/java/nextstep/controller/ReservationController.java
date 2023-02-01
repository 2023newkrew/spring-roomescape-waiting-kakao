package nextstep.controller;

import auth.domain.annotation.LoginMember;
import auth.domain.annotation.Secured;
import auth.domain.enumeration.Roles;
import auth.domain.persist.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
import nextstep.domain.persist.Reservation;
import nextstep.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    public final ReservationService reservationService;

    @Operation(summary = "예약 생성 API")
    @PostMapping
    public ResponseEntity createReservation(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        Long id = reservationService.create(userDetails, reservationRequest);
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @Operation(summary = "테마 ID와 날짜로 예약 조회")
    @GetMapping
    public ResponseEntity readReservations(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationResponse> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "현재 유저의 모든 예약 조회")
    @GetMapping("/mine")
    public ResponseEntity getReservations(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationService.findAllByUserId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "예약 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.deleteById(userDetails, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예약 승인 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @PatchMapping("/{id}/approve")
    public ResponseEntity approveReservation(@PathVariable Long id) {
        reservationService.approveById(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 취소 API")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity cancelReservation(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.cancelById(userDetails, id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 거절 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @PatchMapping("/{id}/reject")
    public ResponseEntity rejectReservation(@PathVariable Long id) {
        reservationService.rejectById(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 취소 승인 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @GetMapping("/{id}/cancel-approve")
    public ResponseEntity approveCancelReservation(@PathVariable Long id) {
        reservationService.approveCancelById(id);

        return ResponseEntity.ok().build();
    }
}
