package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Client;
import lombok.*;

@Setter
@Getter
@Builder
public class ClientDTO {

    private Long id;
    private String loginId;
    private String password;
    private int point;
    private String securityNm;
    private String name;
    private String phoneNm;
    private char isMember;
    private String membership;

    public static ClientDTO from(Client entity) {

        return  ClientDTO.builder()
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
