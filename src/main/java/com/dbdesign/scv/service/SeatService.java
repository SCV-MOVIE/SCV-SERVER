package com.dbdesign.scv.service;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Theater;
import com.dbdesign.scv.entity.TicketSeat;
import com.dbdesign.scv.repository.SeatRepository;
import com.dbdesign.scv.repository.TheaterRepository;
import com.dbdesign.scv.repository.TicketSeatRepository;
import com.dbdesign.scv.util.TicketStatus;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final TheaterRepository theaterRepository;

    public SeatService(SeatRepository seatRepository, TicketSeatRepository ticketSeatRepository, TheaterRepository theaterRepository) {
        this.seatRepository = seatRepository;
        this.ticketSeatRepository = ticketSeatRepository;
        this.theaterRepository = theaterRepository;
    }

    // 좌석 여부(예매 여부 반환)
    public boolean isReserved(String theaterId, String seatNm) {

        Theater theater = theaterRepository.findTheaterById(Long.parseLong(theaterId));

        // 상영관이 존재하지 않는 경우
        if (theater == null) { // 받은 id 로 상영관이 존재하는 지 확인
            throw new IllegalArgumentException("상영관이 존재하지 않습니다.");
        }

        int targetLength = 4; // 목표하는 길이

        if (seatNm.length() < targetLength) {
            int paddingLength = targetLength - seatNm.length();
            StringBuilder paddedSeatNm = new StringBuilder(seatNm);

            for (int i = 0; i < paddingLength; i++) {
                paddedSeatNm.append(' '); // 공백 추가
            }

            seatNm = paddedSeatNm.toString();
        }

        Seat seat = seatRepository.findSeatBySeatNmAndTheater(seatNm, theater);

        TicketSeat ticketSeat = ticketSeatRepository.findTicketSeatBySeat(seat);

        return ticketSeat != null && !ticketSeat.getTicket().getStatus().equals(TicketStatus.REJECTED) && !ticketSeat.getTicket().getStatus().equals(TicketStatus.CANCELLED);
    }
}
