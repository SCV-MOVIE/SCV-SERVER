package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandleTicketDTO {

    @ApiModelProperty(value = "은행 id(Primary Key)", example = "1", required = true)
    private int bankId;

    @ApiModelProperty(value = "티켓 상태", example = "PAYED", required = true)
    private String status;
}
