package nextstep.waiting.controller;

import lombok.RequiredArgsConstructor;
import nextstep.waiting.service.WaitingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/reservation-waitings")
@RestController
public class WaitingController {

    private static final String WAITING_PATH = "/reservation-waitings/";

    private final WaitingService service;
}
