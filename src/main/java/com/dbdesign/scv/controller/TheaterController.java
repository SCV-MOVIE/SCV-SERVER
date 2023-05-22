package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.TheaterFormDTO;
import com.dbdesign.scv.dto.UpdateTheaterFormDTO;
import com.dbdesign.scv.service.TheaterService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theater")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // 상영관 등록
    @PostMapping
    @ApiOperation(value = "새로운 상영관 생성", notes = "행, 열, 이름, 테마를 입력받아 새로운 좌석과 상영관을 생성합니다.")
    public ResponseEntity<Void> makeTheater(@Validated @RequestBody TheaterFormDTO theaterFormDTO) {

        theaterService.makeTheater(theaterFormDTO);
        return ResponseEntity.ok().build();
    }

    // 상영관 좌석 수와 배치 수정 -> 새로운 상영관 생성 (어드민)
    @PostMapping("/update")
    @ApiOperation(value = "상영관 좌석 수와 배치 수정 & 새로운 상영관 생성", notes = "기존의 상영관은 이전 기록 조회를 위해 deleted 상태로 바꾸고, 새로운 상영관과 좌석이 만들어진다.")
    public ResponseEntity<Void> updateTheater(@Validated @RequestBody UpdateTheaterFormDTO updateTheaterFormDTO) {

        theaterService.updateTheater(updateTheaterFormDTO);
        return ResponseEntity.ok().build();
    }
}
