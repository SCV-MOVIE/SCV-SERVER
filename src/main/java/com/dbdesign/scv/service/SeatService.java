package com.dbdesign.scv.service;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Theater;
import com.dbdesign.scv.repository.SeatRepository;
import com.dbdesign.scv.repository.TheaterRepository;
import com.dbdesign.scv.repository.TicketSeatRepository;
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

        Seat seat = seatRepository.findSeatBySeatNmAndTheater(seatNm, theater);

        return ticketSeatRepository.existsBySeat(seat);
    }
}
