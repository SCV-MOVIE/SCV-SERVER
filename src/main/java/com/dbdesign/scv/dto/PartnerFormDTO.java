package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerFormDTO {

    @ApiModelProperty(value = "제휴사 이름", example = "Toss", required = true)
    private String name;

    @ApiModelProperty(value = "할인 금액", example = "1000", required = true)
    private int discount;
}
