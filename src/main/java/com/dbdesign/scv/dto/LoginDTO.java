package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Setter
@Getter
public class LoginDTO { // 로그인 폼

    @ApiModelProperty(value = "로그인 아이디", example = "user", required = true)
    private String loginId;

    @ApiModelProperty(value = "로그인 비밀번호", example = "1234", required = true)
    private String password;
}
