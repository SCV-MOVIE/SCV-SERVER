package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.ShowtimeDTO;
import com.dbdesign.scv.dto.ShowtimeFormDTO;
import com.dbdesign.scv.dto.UpdateShowtimeDTO;
import com.dbdesign.scv.service.ShowtimeService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtime")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    // 상영일정 등록 (어드민)
    @PostMapping
    @ApiOperation(value = "상영일정 등록 (어드민)", notes = "영화 관리자는 영화번호, 상영관번호, 상영일, 상영회차, 상영시작시간을 입력해 상영 일정을 등록합니다.")
    public ResponseEntity<Void> registerShowtime(@Validated @RequestBody ShowtimeFormDTO showtimeFormDTO) {

        showtimeService.registerShowtime(showtimeFormDTO);
        return ResponseEntity.ok().build();
    }

    // 상영일정 공개 (어드민)
    @PatchMapping("/public/{showtimeId}")
    @ApiOperation(value = "상영일정 공개", notes = "영화 관리자는 상영 일정 조회가 가능하도록 공개할 수 있습니다.")
    @ApiImplicitParam(
            name = "showtimeId"
            , value = "상영 일정 id(Primary Key)"
            , required = true
            , dataType = "Long"
            , paramType = "path"
            , defaultValue = "None"
            , example = "1")
    public ResponseEntity<Void> openShowtime(@PathVariable String showtimeId) {

        showtimeService.openShowtime(showtimeId);
        return ResponseEntity.ok().build();
    }

    // 상영일정 수정 (어드민)
    @PatchMapping
    @ApiOperation(value = "상영일정 수정 (어드민)", notes = "영화 관리자는 상영 일정을 수정할 수 있으며, 상영되지 않은 회차이며 예매된 티켓이 없는 경우 수정이 가능합니다. \n" + "" +
            "")
    public ResponseEntity<Void> updateShowtime(@RequestBody UpdateShowtimeDTO updateShowtimeDTO) {

        showtimeService.updateShowtime(updateShowtimeDTO);
        return ResponseEntity.ok().build();
    }

    // 상영일정 삭제 (어드민)
    @DeleteMapping("/{showtimeId}")
    @ApiOperation(value = "상영일정 삭제 (어드민)", notes = "물리적 삭제를 하며, 상영되지 않은 회차이며 예매된 티켓이 없는 경우 삭제가 가능합니다.")
    @ApiImplicitParam(
            name = "showtimeId"
            , value = "상영 일정 id(Primary Key)"
            , required = true
            , dataType = "Long"
            , paramType = "path"
            , defaultValue = "None"
            , example = "1")
    public ResponseEntity<Void> deleteShowtime(@PathVariable String showtimeId) {

        showtimeService.deleteShowtime(showtimeId);
        return ResponseEntity.ok().build();
    }

    // 상영일정 리스트 반환 (어드민)
    @GetMapping("/list")
    @ApiOperation(value = "모든 상영일정 리스트 반환 (어드민)", notes = "모든 상영일정 리스트를 반환합니다.")
    public ResponseEntity<List<ShowtimeDTO>> showtimeList() {

        return ResponseEntity.ok().body(showtimeService.showtimeList());
    }

    // 상영일정 리스트 반환
    @GetMapping("/public-list")
    @ApiOperation(value = "공개된 상영일정 리스트 반환", notes = "공개된 상영일정 리스트를 반환합니다.")
    public ResponseEntity<List<ShowtimeDTO>> publicShowtimeList() {

        return ResponseEntity.ok().body(showtimeService.publicShowtimeList());
    }

    // 상영 일정의 시작 시간 추천 (어드민)
    @GetMapping("/suggest/startDate/{movieId}")
    @ApiOperation(value = "상영 일정의 시작 시간 추천 (yyyy-MM-dd HH:mm)", notes = "휴식시간은 20분으로 산정하며 상영 일정을 추가/수정 시 활용합니다.")
    @ApiImplicitParam(
            name = "movieId"
            , value = "영화 id(Primary Key)"
            , required = true
            , dataType = "Long"
            , paramType = "path"
            , defaultValue = "None"
            , example = "1")
    public ResponseEntity<String> suggestStartDateTime(@PathVariable String movieId) {

        return ResponseEntity.ok().body(showtimeService.suggestStartDateTime(movieId));
    }
}
