package nextstep.controller;

import auth.domain.annotation.LoginMember;
import auth.domain.annotation.Secured;
import auth.domain.enumeration.Roles;
import auth.domain.persist.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.ReservationRequest;
import nextstep.domain.dto.response.ReservationResponse;
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
    public ResponseEntity reservationSave(@LoginMember UserDetails userDetails, @RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.created(URI.create("/" + reservationService.addReservation(userDetails, reservationRequest))).build();
    }

    @Operation(summary = "테마 ID와 날짜로 예약 조회")
    @GetMapping
    public ResponseEntity reservationListByThemeIdAndDate(@RequestParam Long themeId, @RequestParam String date) {
        List<ReservationResponse> results = reservationService.findAllByThemeIdAndDate(themeId, date);
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "현재 유저의 모든 예약 조회")
    @GetMapping("/mine")
    public ResponseEntity reservationListCurrentUser(@LoginMember UserDetails userDetails) {
        List<ReservationResponse> results = reservationService.findAllByUserId(userDetails.getId());
        return ResponseEntity.ok().body(results);
    }

    @Operation(summary = "예약 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity reservationRemove(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.removeReservation(userDetails, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예약 승인 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @PatchMapping("/{id}/approve")
    public ResponseEntity reservationApprove(@PathVariable Long id) {
        reservationService.approveReservation(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 취소 API")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity reservationCancel(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        reservationService.cancelReservation(userDetails, id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 거절 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @PatchMapping("/{id}/reject")
    public ResponseEntity reservationReject(@PathVariable Long id) {
        reservationService.rejectReservation(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "예약 취소 승인 API (관리자 권한 필요)")
    @Secured(role = Roles.ADMIN)
    @GetMapping("/{id}/cancel-approve")
    public ResponseEntity reservationApproveCancel(@PathVariable Long id) {
        reservationService.approveCancelReservation(id);

        return ResponseEntity.ok().build();
    }
}
