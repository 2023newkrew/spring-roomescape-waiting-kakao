package nextstep.controller;

import auth.domain.UserDetails;
import auth.support.template.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.controller.dto.request.WaitingRequest;
import nextstep.controller.dto.response.WaitingResponse;
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

    @PostMapping
    public ResponseEntity<?> createWaiting(@LoginMember UserDetails userDetails,
                                           @RequestBody WaitingRequest waitingRequest) {
        String location = waitingService.create(userDetails, waitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getWaiting(@LoginMember UserDetails userDetails) {
        List<WaitingResponse> waitings = waitingService.findAll(userDetails.getId());
        return ResponseEntity.ok().body(waitings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWaiting(@LoginMember UserDetails userDetails, @PathVariable Long id) {
        waitingService.deleteById(userDetails, id);
        return ResponseEntity.noContent().build();
    }
}
