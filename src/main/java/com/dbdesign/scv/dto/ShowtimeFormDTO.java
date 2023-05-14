package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ShowtimeFormDTO { // 상영일정 등록 폼

    @NotBlank(message = "영화번호(Primary Key) 기입해주세요.")
    @ApiModelProperty(value = "영화번호", example = "1", required = true)
    private int movieId;

    @NotBlank(message = "상영관번호(Primary Key) 기입해주세요.")
    @ApiModelProperty(value = "상영관번호", example = "1", required = true)
    private int theaterId;

    @NotBlank(message = "상영일을 기입해주세요")
    @ApiModelProperty(value = "상영일", example = "2023-01-01", required = true)
    private String startDate;

    @NotBlank(message = "상영회차를 기입해주세요.")
    @ApiModelProperty(value = "상영회차", example = "1", required = true)
    private int round;

    @NotBlank(message = "24시 형식을 따라 상영시작시간을 기입해주세요.")
    @ApiModelProperty(value = "상영시작시간", example = "10:00", required = true)
    private String startTime;
}
