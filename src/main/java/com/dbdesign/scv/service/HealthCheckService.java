package com.dbdesign.scv.service;


import com.dbdesign.scv.dto.HealthCheckDTO;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {
    public HealthCheckDTO returnOkay() {

        HealthCheckDTO healthcheckDTO = new HealthCheckDTO();
        healthcheckDTO.setResultOkay("okay");

        return healthcheckDTO;
    }
}