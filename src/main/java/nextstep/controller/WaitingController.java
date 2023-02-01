package nextstep.controller;

import auth.domain.annotation.LoginMember;
import auth.domain.persist.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.request.WaitingRequest;
import nextstep.domain.dto.response.WaitingResponse;
import nextstep.service.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
public class WaitingController {
    private final WaitingService waitingService;

    @Operation(summary = "예약 대기 생성 API")
    @PostMapping
    public ResponseEntity createWaiting(@LoginMember UserDetails userDetails,
                                        @RequestBody WaitingRequest waitingRequest) {
        String location = waitingService.create(userDetails, waitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @Operation(summary = "자신의 예약 대기 조회 API")
    @GetMapping("/mine")
    public ResponseEntity getWaiting(@LoginMember UserDetails userDetails) {
        List<WaitingResponse> waitings = waitingService.findAll(userDetails.getId());
        return ResponseEntity.ok().body(waitings);
    }

    @Operation(summary = "자신의 예약 대기 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWaiting(@LoginMember UserDetails userDetails,
                                        @PathVariable Long id) {
        waitingService.deleteById(userDetails, id);
        return ResponseEntity.noContent().build();
    }
}
