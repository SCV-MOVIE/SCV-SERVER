package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Bank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BankDTO {

    private int bankId;
    private String approveNm;
    private String source;
    private String destination;
    private int price;
    private String createdAt;
    private String updatedAt;
    private String status;
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
