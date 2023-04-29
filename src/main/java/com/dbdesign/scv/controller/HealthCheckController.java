package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.HealthCheckDTO;
import com.dbdesign.scv.service.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    private final HealthCheckService HealthCheckService;

    public HealthCheckController(com.dbdesign.scv.service.HealthCheckService HealthCheckService) {
        this.HealthCheckService = HealthCheckService;
    }

    @GetMapping("/api/healthcheck")
    public ResponseEntity<HealthCheckDTO> returnOkay() {

        return ResponseEntity.ok().body(HealthCheckService.returnOkay());
    }
}
