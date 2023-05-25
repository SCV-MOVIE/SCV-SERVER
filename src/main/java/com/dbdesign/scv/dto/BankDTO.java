package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Bank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankDTO {

    @ApiModelProperty(value = "은행 id(Primary Key)", example = "1", required = true)
    private int bankId;

    @ApiModelProperty(value = "승인번호", example = "16자리 문자열", required = true)
    private String approveNm;

    @ApiModelProperty(value = "고객의 카드번호 or 계좌번호", example = "010-1234-5678", required = true)
    private String source;

    @ApiModelProperty(value = "SCV 계좌번호", example = "010-1234-5678", required = true)
    private String destination;

    @ApiModelProperty(value = "결제 금액", example = "20000", required = true)
    private int price;

    @ApiModelProperty(value = "생성 일자", example = "yyyy-MM-dd HH:mm:ss", required = true)
    private String createdAt;

    @ApiModelProperty(value = "갱신 일자", example = "yyyy-MM-dd HH:mm:ss", required = true)
    private String updatedAt;

    @ApiModelProperty(value = "상태", example = "STANDBY", required = true)
    private String status;

    @ApiModelProperty(value = "결제 방법(계좌 혹은 카드)", example = "ACCOUNT", required = true)
    private String method;

    public static BankDTO from(Bank bank) {

        return BankDTO.builder()
                .bankId(bank.getId().intValue())
                .approveNm(null) // 서비스 단에서 bank 에 approveNm 있으면 주입할 것
                .source(bank.getSource())
                .destination(bank.getDestination())
                .price(bank.getPrice())
                .createdAt(bank.getCreatedAt())
                .updatedAt(bank.getUpdatedAt())
                .status(bank.getStatus())
                .method(bank.getMethod())
                .build();
    }
}
