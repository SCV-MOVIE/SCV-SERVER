package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateShowtimeDTO {

    @ApiModelProperty(value = "수정할 상영 일정 id(Primary Key)", example = "1", required = true)
    private int showtimeId;

    @ApiModelProperty(value = "새로운 상영 시작 일시", example = "2023-06-30 10:00", required = true)
    private String startDate;

    @ApiModelProperty(value = "새로운 영화별 회차", example = "1", required = true)
    private int round;

    @ApiModelProperty(value = "새로운 영화 id(Primary Key)", example = "1", required = true)
    private int movieId;

    @ApiModelProperty(value = "새로운 상영관 id(Primary Key)", example = "1", required = true)
    private int theaterId;
}
