package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserInfoDTO {

    @ApiModelProperty(value = "유저 고유 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "새로운 로그인 아이디", example = "user", required = true)
    private String newLoginId;

    @ApiModelProperty(value = "새로운 유저 이름", example = "garfield", required = true)
    private String newName;

    @ApiModelProperty(value = "새로운 전화번호", example = "010-1234-5678", required = true)
    private String newPhoneNm;
}
