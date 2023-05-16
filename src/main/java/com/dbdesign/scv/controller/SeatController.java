package com.dbdesign.scv.controller;

import com.dbdesign.scv.service.SeatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // 좌석여부를 알려줌 (어드민)
    @GetMapping("/{theaterId}/{seatNm}")
    @ApiOperation(value = "좌석 여부", notes = "boolean 값으로 입력받은 상영관번호와 좌석번호로 예매가 되었는지 알려줍니다. 예매 완료 시 true")
    public ResponseEntity<Boolean> isReserved(@PathVariable Long theaterId, @PathVariable String seatNm) {

        return ResponseEntity.ok().body(seatService.isReserved(theaterId, seatNm));
    }
}
