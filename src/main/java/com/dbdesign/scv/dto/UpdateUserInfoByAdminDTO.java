package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserInfoByAdminDTO {

    @ApiModelProperty(value = "유저 고유 id(Primary Key)", example = "1", required = true)
    private Long clientId;

    @ApiModelProperty(value = "회원 등급", example = "VIP", required = true)
    private String newMembership;

    @ApiModelProperty(value = "포인트", example = "1000", required = true)
    private int newPoint;
}
