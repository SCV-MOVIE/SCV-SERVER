package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateInfoDTO {

    @ApiModelProperty(value = "고객 이름", example = "garfield", required = true)
    private String name;

    @ApiModelProperty(value = "주민등록번호", example = "000000-0000000", required = true)
    private String securityNm;

    @ApiModelProperty(value = "전화번호", example = "010-1234-5678", required = true)
    private String phoneNm;
}
