package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.ShowtimeDTO;
import com.dbdesign.scv.dto.ShowtimeFormDTO;
import com.dbdesign.scv.dto.UpdateShowtimeDTO;
import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Showtime;
import com.dbdesign.scv.entity.Theater;
import com.dbdesign.scv.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShowtimeService {

    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final TicketSeatRepository ticketSeatRepository;

    public ShowtimeService(TheaterRepository theaterRepository, ShowtimeRepository showtimeRepository, MovieRepository movieRepository, TicketRepository ticketRepository, SeatRepository seatRepository, TicketSeatRepository ticketSeatRepository) {
        this.theaterRepository = theaterRepository;
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.ticketSeatRepository = ticketSeatRepository;
    }

    // 상영일정 등록
    @Transactional
    public void registerShowtime(ShowtimeFormDTO showtimeFormDTO) {

        Movie movie = movieRepository.findMovieById((long) showtimeFormDTO.getMovieId());
        Theater theater = theaterRepository.findTheaterById((long) showtimeFormDTO.getTheaterId());

        // 상영일정 시간 (yyyy-MM-dd HH:mm)
        String startDateTime = showtimeFormDTO.getStartDate() + " " + showtimeFormDTO.getStartTime();

        // 상영일정에 등록할 영화가 존재하지 않는 경우
        if (movie == null) { // 받은 id 로 영화가 존재하는 지 확인
            throw new IllegalArgumentException("상영일정에 등록할 영화가 존재하지 않습니다.");
        }

        // 상영일정에 등록할 상영관이 존재하지 않는 경우
        if (theater == null) { // 받은 id 로 상영일정이 존재하는 지 확인
            throw new IllegalArgumentException("상영일정에 등록할 상영관이 존재하지 않습니다.");
        }

        // 해당 상영일, 상영시간에 상영하지 않는 상영관이 있어야 상영일정을 입력할 수 있습니다.
        for (Showtime showtime : showtimeRepository.findAll()) {

            // 범위의 시작 시간
            String rangeStartDateTime = showtime.getStartDate();

            // 범위의 끝 시간 계산
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

            // 등록할 상영일정의 일자가 이미 상영되고 있는 시간에 속하는 지 확인
            try {
                Date formattedStartDateTime = format.parse(startDateTime);
                if (formattedStartDateTime.compareTo(rangeStartDate) >= 0 && formattedStartDateTime.compareTo(rangeEndDate) < 0) {
                    throw new IllegalArgumentException("해당 상영관은 상영 일정이 있습니다.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Showtime showtime = Showtime.builder()
                .startDate(startDateTime)
                .round(showtimeFormDTO.getRound())
                .isPublic('N')
                .isSoldOut('N')
                .movie(movie)
                .theater(theater)
                .build();

        showtimeRepository.save(showtime);
    }

    // 상영일정 공개
    @Transactional
    public void openShowtime(String showtimeId) {

        Showtime showtime = showtimeRepository.findShowtimeById(Long.valueOf(showtimeId));

        // 상영일정이 존재하지 않는 경우
        if (showtime == null) { // 받은 id 로 상영일정이 존재하는 지 확인
            throw new IllegalArgumentException("상영일정이 존재하지 않습니다.");
        }

        // 이미 공개된 상영 일정인 경우
        if (showtime.getIsPublic() == 'Y') {
            throw new IllegalArgumentException("이미 공개된 상영일정입니다.");
        }

        showtime.setIsPublic('Y');

        showtimeRepository.save(showtime);
    }

    // 상영일정 수정
    @Transactional
    public void updateShowtime(UpdateShowtimeDTO updateShowtimeDTO) {

        Showtime showtime = showtimeRepository.findShowtimeById((long) updateShowtimeDTO.getShowtimeId());

        // 상영일정이 존재하지 않는 경우
        if (showtime == null) { // 받은 id 로 상영일정이 존재하는 지 확인
            throw new IllegalArgumentException("상영일정이 존재하지 않습니다.");
        }

        // 이미 공개된 상영 일정인 경우
        if (showtime.getIsPublic() == 'Y') {
            throw new IllegalArgumentException("이미 공개된 상영일정입니다.");
        }

        // 예매 티켓이 있는 상영일정을 수정하려는 경우
        if (ticketRepository.findAllByShowtime(showtime) != null) {
            throw new IllegalArgumentException("예매 티켓이 있어 수정 불가능한 상영일정입니다.");
        }

        // 상영 일정 수정 및 db 반영
        showtime.setStartDate(updateShowtimeDTO.getStartDate());
        showtime.setRound(updateShowtimeDTO.getRound());
        showtime.setMovie(movieRepository.findMovieById((long) updateShowtimeDTO.getMovieId()));
        showtime.setTheater(theaterRepository.findTheaterById((long) updateShowtimeDTO.getShowtimeId()));

        showtimeRepository.save(showtime);
    }

    // 상영일정 삭제
    @Transactional
    public void deleteShowtime(String showtimeId) {

        Showtime showtime = showtimeRepository.findShowtimeById(Long.valueOf(showtimeId));

        // 상영일정이 존재하지 않는 경우
        if (showtime == null) { // 받은 id 로 상영일정이 존재하는 지 확인
            throw new IllegalArgumentException("상영일정이 존재하지 않습니다.");
        }

        // 이미 공개된 상영 일정인 경우
        if (showtime.getIsPublic() == 'Y') {
            throw new IllegalArgumentException("이미 공개된 상영일정입니다.");
        }

        // 예매 티켓이 있는 상영일정을 수정하려는 경우
        if (ticketRepository.findAllByShowtime(showtime) != null) {
            throw new IllegalArgumentException("예매 티켓이 있어 수정 불가능한 상영일정입니다.");
        }

        // 상영일정 삭제
        showtimeRepository.delete(showtime);
    }

    // 모든 상영일정 리스트 반환
    public List<ShowtimeDTO> showtimeList() {

        List<ShowtimeDTO> showtimeDTOList = new ArrayList<>();

        for (Showtime showtime : showtimeRepository.findAll()) {

            int remainedSeatNm = 0;

            // 예약된 좌석 수 = 상영관의 모든 좌석 중, ticket_seat 테이블에 존재하는 행의 수
            for (Seat seat : seatRepository.findAllByTheater(showtime.getTheater())
            ) {
                if (!ticketSeatRepository.existsBySeat(seat)) {
                    remainedSeatNm++;
                }
            }

            ShowtimeDTO showtimeDTO = ShowtimeDTO.from(showtime);
            showtimeDTO.setRemainSeatNm(remainedSeatNm);
            showtimeDTO.setTheaterSize(seatRepository.findAllByTheater(showtime.getTheater()).size());
            showtimeDTO.setMovieName(showtime.getMovie().getName());
            showtimeDTO.setTheaterName(showtime.getTheater().getName());

            showtimeDTOList.add(showtimeDTO);
        }

        return showtimeDTOList;
    }

    // 공개된 상영일정 리스트 반환
    public List<ShowtimeDTO> publicShowtimeList() {

        List<ShowtimeDTO> showtimeDTOList = new ArrayList<>();

        for (Showtime showtime : showtimeRepository.findAll()) {

            if (showtime.getIsPublic() == 'Y') { // 공개된 상영일정에 대해서만 로직 적용
                int remainedSeatNm = 0;

                // 예약된 좌석 수 = 상영관의 모든 좌석 중, ticket_seat 테이블에 존재하는 행의 수
                for (Seat seat : seatRepository.findAllByTheater(showtime.getTheater())
                ) {
                    if (!ticketSeatRepository.existsBySeat(seat)) {
                        remainedSeatNm++;
                    }
                }

                ShowtimeDTO showtimeDTO = ShowtimeDTO.from(showtime);
                showtimeDTO.setRemainSeatNm(remainedSeatNm);
                showtimeDTO.setTheaterSize(seatRepository.findAllByTheater(showtime.getTheater()).size()); // 상영관으로 모든 좌석을 가져와서 크기를 넣음
                showtimeDTO.setMovieName(showtime.getMovie().getName());
                showtimeDTO.setTheaterName(showtime.getTheater().getName());

                showtimeDTOList.add(showtimeDTO);
            }
        }

        return showtimeDTOList;
    }
}
