package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Client;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ClientDTO {

    @ApiModelProperty(value = "고객 id(Primary Key)", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "로그인 아이디", example = "garfield", required = true)
    private String loginId;

    @ApiModelProperty(value = "로그인 비밀번호", example = "1234", required = true)
    private String password;

    @ApiModelProperty(value = "포인트", example = "2000", required = true)
    private int point;

    @ApiModelProperty(value = "주민등록번호", example = "000000-0000000", required = true)
    private String securityNm;

    @ApiModelProperty(value = "고객 이름", example = "venom", required = true)
    private String name;

    @ApiModelProperty(value = "전화번호", example = "010-1234-5678", required = true)
    private String phoneNm;

    @ApiModelProperty(value = "회원 여부", example = "Y", required = true)
    private char isMember;

    @ApiModelProperty(value = "고객 등급", example = "VIP", required = true)
    private String membership;

    public static ClientDTO from(Client entity) {

        return ClientDTO.builder()
                .id(entity.getId())
                .loginId(entity.getLoginId())
                .password("")
                .point(entity.getPoint())
                .securityNm("")
                .name(entity.getName())
                .phoneNm(entity.getPhoneNm())
                .isMember(entity.getIsMember())
                .membership(entity.getMembership())
                .build();
    }
}
