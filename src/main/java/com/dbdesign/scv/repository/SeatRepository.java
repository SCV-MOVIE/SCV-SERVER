package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByTheater(Theater theater);

    Seat findSeatBySeatNmAndTheater(String seatNm, Theater theater);
}
