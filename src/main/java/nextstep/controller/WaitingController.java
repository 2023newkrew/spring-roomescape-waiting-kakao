package nextstep.controller;

import auth.domain.persist.UserDetails;
import auth.domain.template.LoginMember;
import lombok.RequiredArgsConstructor;
import nextstep.domain.dto.WaitingRequest;
import nextstep.service.WaitingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public ResponseEntity createWaiting(@LoginMember UserDetails userDetails,
                                        @RequestBody WaitingRequest waitingRequest) {
        String location = waitingService.create(userDetails, waitingRequest);
        return ResponseEntity.created(URI.create(location)).build();
    }
}
