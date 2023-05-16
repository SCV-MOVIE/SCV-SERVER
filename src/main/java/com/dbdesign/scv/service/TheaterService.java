package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.TheaterFormDTO;
import com.dbdesign.scv.dto.UpdateTheaterFormDTO;
import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Showtime;
import com.dbdesign.scv.entity.Theater;
import com.dbdesign.scv.entity.TheaterType;
import com.dbdesign.scv.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TicketRepository ticketRepository;
    private final TheaterTypeRepository theaterTypeRepository;


    public TheaterService(TheaterRepository theaterRepository, SeatRepository seatRepository, ShowtimeRepository showtimeRepository, TicketRepository ticketRepository, TheaterTypeRepository theaterTypeRepository) {
        this.theaterRepository = theaterRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
        this.ticketRepository = ticketRepository;
        this.theaterTypeRepository = theaterTypeRepository;
    }

    // 상영관 좌석 수와 배치 수정 & 새로운 상영관 생성
    @Transactional
    public void updateTheater(UpdateTheaterFormDTO updateTheaterFormDTO) {

        Theater theater = theaterRepository.findTheaterById((long) updateTheaterFormDTO.getTheaterId());
        TheaterType theaterType = theaterTypeRepository.findTheaterTypeByName(updateTheaterFormDTO.getName());

        // 새로 만들어질 상영관의 테마가 존재하지 않는 테마일 경우
        if (theaterType == null) {
            throw new IllegalArgumentException("새로 만들어질 상영관의 테마가 존재하지 않는 테마입니다.");
        }

        // 수정할 상영관이 존재하지 않는 경우
        if (theater == null || theater.getDeleted() == 'Y') { // 받은 id 로 상영관이 존재하는 지 확인
            throw new IllegalArgumentException("수정할 상영관이 존재하지 않습니다.");
        }

        // 상영관 수정 요청 시간 (yyyy-MM-dd HH:mm)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date formattedRequestDateTime;
        try {
            formattedRequestDateTime = format.parse(String.valueOf(LocalDateTime.now()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 수정될 상영관에 예매된 티켓이 있는 경우 or 수정될 상영관이 요청 시점에 이미 상영되고 있는 경우
        for (Showtime showtime : showtimeRepository.findAll()) {

            if (ticketRepository.findAllByShowtime(showtime).size() != 0) {
                throw new IllegalArgumentException("예매된 티켓이 있는 상영관은 수정할 수 없습니다.");
            }

            // 범위의 시작 시간
            String rangeStartDateTime = showtime.getStartDate();

            // 범위의 끝 시간 계산
            Calendar calendar = Calendar.getInstance();
            Date rangeStartDate;
            try {
                rangeStartDate = format.parse(rangeStartDateTime);
                calendar.setTime(rangeStartDate);
                calendar.add(Calendar.MINUTE, showtime.getMovie().getLength());
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Date rangeEndDate = calendar.getTime();

            // 수정할 상영관이 이미 상영 중인지 확인
            if (formattedRequestDateTime.compareTo(rangeStartDate) >= 0 && formattedRequestDateTime.compareTo(rangeEndDate) < 0 && showtime.getTheater().getName().equals(theater.getName())) {
                throw new IllegalArgumentException("상영 중에 있는 상영관은 수정할 수 없습니다.");
            }
        }

        // 수정될 상영관은 논리적 삭제
        theater.setDeleted('Y');
        theaterRepository.save(theater);

        // 폼에서 받은 데이터로 새로운 theater 생성
        Theater newTheater = new Theater();
        newTheater.setLayout(updateTheaterFormDTO.getRow() + "x" + updateTheaterFormDTO.getColumn());
        newTheater.setName(updateTheaterFormDTO.getName());
        newTheater.setDeleted('N');
        newTheater.setTheaterType(theaterType);

        theaterRepository.save(newTheater);

        // 폼에서 받은 데이터로 seat 생성
        createSeats(updateTheaterFormDTO.getRow(), updateTheaterFormDTO.getColumn(), newTheater);
    }

    // 상영관, 행, 열 값을 받아 좌석 생성
    @Transactional
    public void createSeats(int row, int column, Theater theater) {

        if (!theaterRepository.existsById(theater.getId()) || theater.getDeleted() == 'Y') {
            throw new IllegalArgumentException("좌석을 생성할 상영관이 존재하지 않습니다.");
        }

        // 좌석 생성
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= column; j++) {
                char seatLetter = (char) ('A' + j - 1);
                String seatName = seatLetter + String.valueOf(i);

                Seat seat = new Seat();
                seat.setTheater(theater);
                seat.setSeatNm(seatName);

                seatRepository.save(seat);
            }
        }
    }

    // 상영관 등록 (좌석 포함)
    @Transactional
    public void makeTheater(TheaterFormDTO theaterFormDTO) {

        TheaterType theaterType = theaterTypeRepository.findTheaterTypeByName(theaterFormDTO.getName());

        // 새로 만들어질 상영관의 테마가 존재하지 않는 테마일 경우
        if (theaterType == null) {
            throw new IllegalArgumentException("새로 만들어질 상영관의 테마가 존재하지 않는 테마입니다.");
        }

        // 폼에서 받은 데이터로 새로운 theater 생성
        Theater newTheater = new Theater();
        newTheater.setLayout(theaterFormDTO.getRow() + "x" + theaterFormDTO.getColumn());
        newTheater.setName(theaterFormDTO.getName());
        newTheater.setDeleted('N');
        newTheater.setTheaterType(theaterType);

        theaterRepository.save(newTheater);

        // 폼에서 받은 데이터로 seat 생성
        createSeats(theaterFormDTO.getRow(), theaterFormDTO.getColumn(), newTheater);
    }
}
