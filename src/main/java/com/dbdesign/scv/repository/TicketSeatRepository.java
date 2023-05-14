package com.dbdesign.scv.repository;

import com.dbdesign.scv.entity.Seat;
import com.dbdesign.scv.entity.TicketSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSeatRepository extends JpaRepository<TicketSeat, Long> {

    boolean existsBySeat(Seat seat);
}
