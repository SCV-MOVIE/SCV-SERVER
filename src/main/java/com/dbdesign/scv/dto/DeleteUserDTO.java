package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteUserDTO {

    @ApiModelProperty(value = "유저 고유 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "비밀번호", example = "1234", required = true)
    private String password;
}
