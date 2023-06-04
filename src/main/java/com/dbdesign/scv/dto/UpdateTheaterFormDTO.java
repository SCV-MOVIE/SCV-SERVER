package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class UpdateTheaterFormDTO {

    @ApiModelProperty(value = "상영관의 id", example = "1", required = true)
    private int theaterId;

    @ApiModelProperty(value = "상영관의 행 수", example = "10", required = true)
    private int row;

    @ApiModelProperty(value = "상영관의 열 수", example = "10", required = true)
    private int column;

    @NotBlank(message = "새로운 상영관의 이름 기입해주세요.")
    @ApiModelProperty(value = "상영관 이름", example = "1관", required = true)
    private String name;

    @NotBlank(message = "새로운 상영관의 테마를 기입해주세요. NORMAL|PREMIUM|3D")
    @ApiModelProperty(value = "상영관의 테마", example = "NORMAL", required = true)
    private String theaterType; // NORMAL, PREMIUM, 3D
}
