package com.dbdesign.scv.dto;

import com.dbdesign.scv.entity.Ticket;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketDTO {

    // 추가한 것
    @ApiModelProperty(value = "티켓 id(Primary Key)", example = "1", required = true)
    private int ticketId;

    @ApiModelProperty(value = "티켓 상태", example = "PRINTED", required = true)
    private String status;

    @ApiModelProperty(value = "티켓 예매 시, 사용한 포인트", example = "1000", required = true)
    private int usedPoint;

    @ApiModelProperty(value = "예약번호", example = "16자리 스트링 값", required = true)
    private String reserveNm;

    // 명세
    @ApiModelProperty(value = "영화 포스터 URL", example = "영화 포스터 URL", required = true)
    private String movieImgUrl;

    @ApiModelProperty(value = "영화 이름", example = "saw", required = true)
    private String movieName;

    @ApiModelProperty(value = "상영관 이름", example = "1관", required = true)
    private String theaterName;

    @ApiModelProperty(value = "인원 수", example = "2", required = true)
    private int peopleNm;

    @ApiModelProperty(value = "좌석 정보", example = "A1, A2", required = true)
    private String seatInfo; // A1, A2 이런식으로 보여짐

    @ApiModelProperty(value = "영화 시작 일시", example = "yyyy-MM-dd HH:mm", required = true)
    private String movieStartTime;

    @ApiModelProperty(value = "영화 길이(min.)", example = "221", required = true)
    private int movieLength;

    @ApiModelProperty(value = "결제 일시", example = "yyyy-MM-dd HH:mm:ss", required = true)
    private String paymentDate;

    @ApiModelProperty(value = "결제 방식", example = "ACCOUNT", required = true)
    private String paymentMethod;

    @ApiModelProperty(value = "티켓 가격", example = "20000", required = true)
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
