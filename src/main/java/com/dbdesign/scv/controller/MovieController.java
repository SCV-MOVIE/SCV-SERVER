package com.dbdesign.scv.controller;

import com.dbdesign.scv.dto.*;
import com.dbdesign.scv.service.MovieService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;


    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // 영화 등록 (어드민)
    @PostMapping
    @ApiOperation(value = "영화 등록 (어드민)", notes = "영화 관리자는 영화명, 상영길이, 등급, 감독명, 배우명, 장르, 영화소개, 배급사, 포스터 URL을 입력해 영화를 등록할 수 있습니다.")
    public ResponseEntity<Void> registerMovie(@RequestBody MovieFormDTO movieFormDTO) {

        movieService.registerMovie(movieFormDTO);
        return ResponseEntity.ok().build();
    }

    // 영화 정보 수정 (어드민)
    @PatchMapping
    @ApiOperation(value = "영화 정보 수정 (어드민)", notes = "영화 관리자는 상영길이, 등급, 감독명, 배우명, 장르, 영화소개, 배급사, 포스터 URL을 새로 입력해 영화 정보를 수정할 수 있습니다.")
    public ResponseEntity<Void> updateMovie(@RequestBody UpdateMovieDTO updateMovieDTO) {

        movieService.updateMovie(updateMovieDTO);
        return ResponseEntity.ok().build();
    }

    // 영화 삭제 (어드민)
    @PatchMapping("/delete")
    @ApiOperation(value = "영화 삭제 (어드민)", notes = "논리적 삭제를 하며, 상영 일정이 없는 영화의 경우에만 삭제가 가능합니다.")
    public ResponseEntity<?> deleteMovie(@RequestBody DeleteMovieDTO deleteMovieDTO) {

        movieService.deleteMovie(deleteMovieDTO);
        return ResponseEntity.ok().build();
    }

    // 영화 리스트 조회
    @GetMapping("/list")
    @ApiOperation(value = "영화 리스트 조회", notes = "모든 영화가 리스트로 반환됩니다.")
    public ResponseEntity<List<MovieDTO>> movieList() {

        return ResponseEntity.ok().body(movieService.movieList());
    }

    // 새로운 장르 등록 (어드민)
    @PostMapping("/genre")
    @ApiOperation(value = "영화 장르 등록 (어드민)", notes = "영화 관리자는 새로운 장르를 등록할 수 있습니다.")
    public ResponseEntity<?> registerGenre(@RequestBody GenreDTO genreDTO) {

        movieService.registerGenre(genreDTO);
        return ResponseEntity.ok().build();
    }

    // 영화 장르 조회 (어드민)
    @GetMapping("/genre/list")
    @ApiOperation(value = "영화 장르 리스트 조회 (어드민)", notes = "영화 관리자는 모든 장르를 조회할 수 있습니다.")
    public ResponseEntity<List<GenreDTO>> genreList() {

        return ResponseEntity.ok().body(movieService.genreList());
    }

    // 영화 장르 삭제 (논리적 삭제, 어드민)
    @PatchMapping("/genre/delete/{name}")
    @ApiOperation(value = "영화 장르 (논리적) 삭제 (어드민)", notes = "영화 관리자는 장르를 삭제할 수 있습니다.")
    public ResponseEntity<Void> deleteGenre(@PathVariable String name) {

        movieService.deleteGenre(name);
        return ResponseEntity.ok().build();
    }
}
