package com.nextstep.interfaces.sales;

import com.nextstep.domains.sales.SalesService;
import com.nextstep.interfaces.sales.dtos.SalesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/sales")
@RestController
public class SalesController {
    private final SalesService service;

    @GetMapping
    public ResponseEntity<List<SalesResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
