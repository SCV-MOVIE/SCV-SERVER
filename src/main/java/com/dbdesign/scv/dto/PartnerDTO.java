package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Partner;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PartnerDTO {

    @ApiModelProperty(value = "제휴사 id(Primary Key)", example = "1", required = true)
    private int partnerId;

    @ApiModelProperty(value = "제휴사 이름", example = "Toss", required = true)
    private String name;

    @ApiModelProperty(value = "할인 금액", example = "1000", required = true)
    private int discount;

    public static PartnerDTO from(Partner entity) {

        return PartnerDTO.builder()
                .partnerId(entity.getId().intValue())
                .name(entity.getName())
                .discount(entity.getDiscount())
                .build();
    }
}
