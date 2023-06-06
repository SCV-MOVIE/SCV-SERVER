package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.TheaterDTO;
import com.dbdesign.scv.dto.TheaterFormDTO;
import com.dbdesign.scv.dto.UpdateTheaterFormDTO;
import com.dbdesign.scv.service.TheaterService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theater")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    // 상영관 등록 (어드민)
    @PostMapping
    @ApiOperation(value = "새로운 상영관 생성 (어드민)", notes = "행, 열, 이름, 테마를 입력받아 새로운 좌석과 상영관을 생성합니다.")
    public ResponseEntity<Void> makeTheater(@RequestBody TheaterFormDTO theaterFormDTO) {

        theaterService.makeTheater(theaterFormDTO);
        return ResponseEntity.ok().build();
    }

    // 상영관 좌석 수와 배치 수정 -> 새로운 상영관 생성 (어드민)
    @PostMapping("/update")
    @ApiOperation(value = "상영관 좌석 수와 배치 수정 & 새로운 상영관 생성 (어드민)", notes = "기존의 상영관은 이전 기록 조회를 위해 deleted 상태로 바꾸고, 새로운 상영관과 좌석이 만들어진다.")
    public ResponseEntity<Void> updateTheater(@Validated @RequestBody UpdateTheaterFormDTO updateTheaterFormDTO) {

        theaterService.updateTheater(updateTheaterFormDTO);
        return ResponseEntity.ok().build();
    }

    // 상영관 삭제 (어드민)
    @PatchMapping("/delete/{theaterId}")
    @ApiOperation(value = "상영관 삭제 (어드민)", notes = "관리자가 상영관을 삭제하면 물리적 삭제가 아닌 논리적인 U만 일어난다. 이때 삭제는 상영일정이 공개되지 않아 예매된 티켓이 없어야 한다.")
    @ApiImplicitParam(
            name = "theaterId"
            , value = "상영관 id(Primary Key)"
            , required = true
            , dataType = "Long"
            , paramType = "path"
            , defaultValue = "None"
            , example = "1")
    public ResponseEntity<Void> deleteTheater(@PathVariable String theaterId) {

        theaterService.deleteTheater(theaterId);
        return ResponseEntity.ok().build();
    }

    // 상영관 리스트 반환
    @GetMapping("/list")
    @ApiOperation(value = "모든 상영관 리스트 반환", notes = "모든 상영관 리스트를 반환합니다.")
    public ResponseEntity<List<TheaterDTO>> theaterList() {

        return ResponseEntity.ok().body(theaterService.showTheaterList());
    }
}
