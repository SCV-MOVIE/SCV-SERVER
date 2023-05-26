package com.dbdesign.scv.controller;

import com.dbdesign.scv.service.SeatService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seat")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // 좌석여부를 알려줌 (어드민)
    @GetMapping("/isReserved/{theaterId}/{seatNm}")
    @ApiOperation(value = "좌석 여부를 boolean 값으로 알려줌 (어드민)", notes = "boolean 값으로 입력받은 상영관번호와 좌석번호로 예매가 되었는지 알려줍니다. 예매 완료 시 true")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "theaterId"
                    , value = "상영관 id(Primary Key)"
                    , required = true
                    , dataType = "Long"
                    , paramType = "path"
                    , defaultValue = "None"
                    , example = "1"
            ),
            @ApiImplicitParam(
                    name = "seatNm"
                    , value = "좌석 번호"
                    , required = true
                    , dataType = "String"
                    , paramType = "path"
                    , defaultValue = "None"
                    , example = "A10"
            )
    })
    public ResponseEntity<Boolean> isReserved(@PathVariable String theaterId, @PathVariable String seatNm) {

        return ResponseEntity.ok().body(seatService.isReserved(theaterId, seatNm));
    }
}
