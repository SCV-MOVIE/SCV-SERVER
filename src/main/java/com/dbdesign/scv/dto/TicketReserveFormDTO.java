package com.dbdesign.scv.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketReserveFormDTO {

    private int showtimeId;
    private PrivateInfoDTO privateInfoDTO; // 회원일 시, null 값
    private List<SeatDTO> seats;
    private int usedPoint;
    private int price;
    private String paymentMethod; // card|account|point
    private String partnerName;
    private String CardOrAccountNm; // 카드/계좌 번호
}
