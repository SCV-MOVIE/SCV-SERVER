package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Showtime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ShowtimeDTO {

    @ApiModelProperty(value = "상영 일정 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "상영 시작 일시", example = "yyyy-MM-dd HH:mm", required = true)
    private String startDate;

    @ApiModelProperty(value = "영화별 회차", example = "1", required = true)
    private int round;

    @ApiModelProperty(value = "공개 여부", example = "Y", required = true)
    private int isPublic;

    @ApiModelProperty(value = "남은 좌석 수", example = "150", required = true)
    private int remainSeatNm; // 남은 좌석 수

    @ApiModelProperty(value = "총 좌석 수", example = "200", required = true)
    private int theaterSize; // 총 좌석 수

    @ApiModelProperty(value = "영화 객체", example = "movieDTO", required = true)
    private MovieDTO movieDTO;

    @ApiModelProperty(value = "상영관 이름", example = "1관", required = true)
    private String theaterName;

    public static ShowtimeDTO from(Showtime entity) {

        return ShowtimeDTO.builder()
                .id(entity.getId())
                .startDate(entity.getStartDate())
                .round(entity.getRound())
                .isPublic(entity.getIsPublic()) // 어드민에서 공개 전환을 위해 보여줄 필요 있음
                .remainSeatNm(0) // service에서 채울 예정
                .theaterSize(0) // service에서 채울 예정
                .movieDTO(MovieDTO.from(entity.getMovie())) // service에서 movie의 장르를 채울 예정
                .theaterName("") // service에서 채울 예정
                .build();
    }
}
