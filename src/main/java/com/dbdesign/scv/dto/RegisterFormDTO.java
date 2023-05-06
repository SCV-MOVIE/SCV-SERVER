package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class RegisterFormDTO { // 회원가입 폼

    @NotBlank(message = "SCV 서비스 로그인 아이디를 기입해주세요.")
    @ApiModelProperty(value = "아이디", example = "user", required = true)
    private String loginId;

    @NotBlank(message = "SCV 서비스 로그인 비밀번호를 입력해주세요.")
    @ApiModelProperty(value = "비밀번호", example = "1234", required = true)
    private String password;

    @NotBlank(message = "이름을 기입해주세요.")
    @ApiModelProperty(value = "이름", example = "garfield", required = true)
    private String name;

    @NotBlank(message = "\"-\"를 포함하여 전화번호를 기입해주세요.")
    @ApiModelProperty(value = "전화번호", example = "010-1234-5678", required = true)
    private String phoneNm;

    @NotBlank(message = "\"-\"를 포함하여 주민번호를 기입해주세요.")
    @ApiModelProperty(value = "주민번호", example = "000000-0000000", required = true)
    private String securityNm;
}
