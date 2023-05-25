package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatDTO {

    @ApiModelProperty(value = "좌석 번호", example = "A1", required = true)
    private String seatNm;

    @ApiModelProperty(value = "상영관 id(Primary Key)", example = "1", required = true)
    private int theaterId;
}
