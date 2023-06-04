package com.dbdesign.scv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketReserveFormDTO {

    @ApiModelProperty(value = "상영 일정 id(Primary Key)", example = "1", required = true)
    private int showtimeId;

    @ApiModelProperty(value = "개인 정보(이름, 주민등록번호, 전화번호)", example = "ACCOUNT", required = true)
    private PrivateInfoDTO privateInfoDTO; // 회원일 시, null 값

    @ApiModelProperty(value = "좌석 리스트", required = true)
    private List<SeatDTO> seats;

    @ApiModelProperty(value = "사용한 포인트", example = "1000", required = true)
    private int usedPoint;

    @ApiModelProperty(value = "총 금액", example = "20000", required = true)
    private int price;

    @ApiModelProperty(value = "결제 방식(CARD|ACCOUNT|POINT)", example = "ACCOUNT", required = true)
    private String paymentMethod; // card|account|point

    @ApiModelProperty(value = "선택한 제휴사", example = "Toss", required = true)
    private String partnerName;

    @ApiModelProperty(value = "카드/계좌 번호", example = "card number", required = true)
    private String CardOrAccountNm; // 카드/계좌 번호
}
