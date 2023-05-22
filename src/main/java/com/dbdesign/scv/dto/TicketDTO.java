package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Ticket;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDTO {

    // 추가한 것
    private int ticketId;
    private String status;
    private int usedPoint;
    private String reserveNm;

    // 명세
    private String movieImgUrl;
    private String movieName;
    private String theaterName;
    private int peopleNm;
    private String seatInfo; // A1, A2 이런식으로 보여짐
    private String movieStartTime;
    private int movieLength;
    private String paymentDate;
    private String paymentMethod;
    private int price;

    public static TicketDTO from(Ticket ticket) {

        return TicketDTO.builder()
                .ticketId(ticket.getId().intValue())
                .status(ticket.getStatus())
                .usedPoint(ticket.getUsedPoint())
                .reserveNm(ticket.getReserveNm())
                .movieImgUrl(ticket.getShowtime().getMovie().getImgUrl()) // TODO: 제대로 주입이 되는 지 확인할 것
                .movieName(ticket.getShowtime().getMovie().getName())
                .theaterName(ticket.getShowtime().getTheater().getName())
                .peopleNm(0) // 서비스 단에서 넣을 것
                .seatInfo("") // 서비스 단에서 넣을 것
                .movieStartTime(ticket.getShowtime().getStartDate())
                .movieLength(ticket.getShowtime().getMovie().getLength())
                .paymentDate(ticket.getPaymentDate())
                .paymentMethod("") // 서비스 단에서 넣을 것
                .price(ticket.getPrice())
                .build();
    }
}
