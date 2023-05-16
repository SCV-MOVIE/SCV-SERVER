package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByTheater(Theater theater);

    Seat findSeatBySeatNmAndTheater(String seatNm, Theater theater);
}
